import React, { useContext, useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { AuthContext } from "../../../util/AuthContext";
import ProfilPreview from "../../Profil/ProfilPreview";
import city_icon from "../../../media/city.png";
import "./InseratDetail.scss";
import InseratBearbeiten from "./InseratBearbeiten";
import { useHistory } from "react-router";

const InseratDetail = () => {
	let history = useHistory();

	const [user] = useContext(AuthContext);

	const [inserat, setInserat] = useState();
	const [ersteller, setErsteller] = useState();
	const [loading, setLoading] = useState(true);
	const [bearbeiten, setBeartbeiten] = useState(false);

	let { id } = useParams();

	const [isFavorite, setFavorite] = useState(false);

	const fetchInserat = () => {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate/" + id;

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "GET",
			credentials: "include",
		})
			.then((response) => response.json())
			.then((data) => {
				setInserat({
					id: data.inseratId,
					land: data.zielLand.name,
					stadt: data.zielStadt.name,
					beschreibung: data.beschreibung,
					maxUser: data.maxUser,
					uni: data.uni,
					vonDat: data.vonDat,
					bisDat: data.bisDat,
					mitglieder: data.mitglieder,
				});

				setErsteller(data.ersteller);
				setLoading(false);
			})
			.catch((error) => {
				console.log(error);
			})
			.finally(() => {
				setLoading(false);
			});
	};

	const fetchGemerkteInserate = () => {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/gemerkt";

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "GET",
			credentials: "include",
		})
			.then((response) => response.json())
			.then((data) => {
				setFavorite(
					data.some((inserat) => {
						return inserat.inseratId == id;
					})
				);
			})
			.catch((error) => {
				console.log(error);
			});
	};

	useEffect(() => {
		fetchInserat();
		fetchGemerkteInserate();
	}, [inserat]);

	const anfragen = () => {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate/" + id;

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "POST",
			credentials: "include",
		}).then((response) => console.log(response));
	};

	const merken = () => {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate/merken/" + id;

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "POST",
			credentials: "include",
		})
			.then(setFavorite(true))
			.catch((err) => console.log(err));
	};

	const entmerken = () => {
		const url =
			"http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate/entmerken/" + id;

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "DELETE",
			credentials: "include",
		})
			.then(setFavorite(false))
			.catch((err) => console.log(err));
	};

	const deleteInserat = () => {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate/" + id;

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "DELETE",
			credentials: "include",
		})
			.then(() => history.push("/suche"))
			.catch((err) => console.log(err));
	};

	const inseratSpeichern = (inserat) => {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate/" + id;

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "PUT",
			credentials: "include",
			body: JSON.stringify(inserat),
		})
			.then((data) => console.log(data))
			.catch((err) => console.log(err));

		setBeartbeiten(false);
	};

	return (
		<>
			{bearbeiten ? (
				<>
					<InseratBearbeiten
						ersteller={ersteller}
						inserat={inserat}
						inseratSpeichern={inseratSpeichern}
						deleteInserat={deleteInserat}
					/>
				</>
			) : (
				<>
					{!loading && (
						<div className="mt-5">
							<div className="image-container">
								{isFavorite ? (
									<span className="icons entmerken" onClick={() => entmerken()}>
										star
									</span>
								) : (
									<span className="icons merken" onClick={() => merken()}>
										star_border
									</span>
								)}

								<div className="image-wrapper">
									<img src={city_icon} alt="city" />
								</div>
							</div>

							<div className="info-block my-3">
								<div className="row">
									<div className="col heading">
										<h3>
											{inserat.stadt}, {inserat.land}
										</h3>
										<h4>
											{inserat.vonDat} - {inserat.bisDat}
										</h4>
									</div>

									<div className="col text-right anfragen">
										{ersteller.userId === user.id ||
										user?.gruppe?.inseratId === inserat.id ? (
											<div />
										) : (
											<button className="btn" onClick={() => anfragen()}>
												Anfragen
											</button>
										)}
									</div>
								</div>

								<div className="stats">
									<p>
										<span className="icons">group</span>
										{inserat.maxUser === 1
											? `${inserat.maxUser} weiterer Buddy`
											: `${inserat.maxUser} weitere Buddys`}
									</p>
									<p>
										<span className="icons">school</span>
										{inserat.uni}
									</p>
								</div>

								<p className="py-3">{inserat.beschreibung}</p>
							</div>

							<ProfilPreview
								id={ersteller.userId}
								profilbildURL={
									ersteller.profilbild?.url
										? `${process.env.PUBLIC_URL}/images/${ersteller.profilbild.url}`
										: false
								}
								name={ersteller.name}
								alter={ersteller.alter}
								studium={ersteller.studium}
								istErsteller={true}
								contactZeigen={user.id !== ersteller.userId}
							/>

							{inserat.mitglieder.map((mitglied, index) => (
								<ProfilPreview
									key={index}
									id={mitglied.userId}
									profilbildURL={
										mitglied.profilbild?.url
											? `${process.env.PUBLIC_URL}/images/${mitglied.profilbild.url}`
											: false
									}
									name={mitglied.name}
									alter={mitglied.alter}
									studium={mitglied.studium}
									istErsteller={false}
									contactZeigen={user.id !== mitglied.userId}
								/>
							))}

							{ersteller.userId == user.id && (
								<div className="user-controls">
									<p onClick={() => setBeartbeiten(true)}>Bearbeiten</p>
									<p onClick={() => deleteInserat()}>Löschen</p>
								</div>
							)}
						</div>
					)}
				</>
			)}
		</>
	);
};

export default InseratDetail;
