import React, { useEffect, useState, useContext } from "react";
import { useParams } from "react-router-dom";
import { AuthContext } from "../../util/AuthContext";
import account_circle from "../../media/account_circle.svg";
import { useHistory } from "react-router";
import "./Profil.scss";

const Profil = () => {
	const [user] = useContext(AuthContext);

	const [loading, setLoading] = useState(true);
	const [profil, setProfil] = useState();
	const [stadt, setStadt] = useState();
	const [land, setLand] = useState();
	const [studienrichtung, setStudienrichtung] = useState();
	const [sprachen, setSprachen] = useState([]);

	const [wantsToJoin, setWantsToJoin] = useState(false);

	let { id } = useParams();
	let history = useHistory();

	const userWantsToJoinMyGroup = () => {
		const url =
			"http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate/reqmine/" + id;

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
				setWantsToJoin(data);
			})
			.catch((err) => {
				setWantsToJoin(false);
				console.log(err);
			});
	};

	const addToGroup = () => {
		const url =
			"http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate/add-to-gruppe/" + id;

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "POST",
			credentials: "include",
		})
			.then((data) => {
				console.log(data);
			})
			.catch((err) => console.log(err));
	};

	const addContact = () => {
		const url =
			"http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/user/add-contact/" + id;

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "PUT",
			credentials: "include",
		})
			.then(() => {
				history.push("/chat");
			})
			.catch((err) => console.log(err));
	};

	useEffect(() => {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/user/" + id;

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
				console.log(data);

				setProfil(data);
				setStadt(data.herkunftsStadt);
				setLand(data.herkunftsLand);
				setStudienrichtung(data.studienrichtung);
				setSprachen(data.sprachen);
				setLoading(false);
			})
			.catch((error) => {
				console.log(error);
			})
			.finally(() => {
				setLoading(false);
			});

		userWantsToJoinMyGroup();
	}, []);

	return (
		<>
			{!loading && (
				<div className="profil">
					<div
						className={
							profil.profilbild?.url ? "image-cropper mt-5" : "account-avatar mt-5"
						}
					>
						<img
							src={
								profil.profilbild?.url
									? `${process.env.PUBLIC_URL}/images/${profil.profilbild.url}`
									: account_circle
							}
							alt="profile"
						/>
					</div>

					<div>
						<div className="profil-content">
							<div>
								<h3>
									{profil.name}, {profil.alter}
								</h3>
								<h4>
									{stadt.name}, {land.name}
								</h4>
								<h5>Studiert {studienrichtung.bezeichnungDeutsch}</h5>
								<h5>{sprachen.map((sprache) => {})}</h5>
							</div>

							<div className="col chat-button">
								{user.id != id && (
									<span className="icons" onClick={() => addContact()}>
										chat_bubble_outline
									</span>
								)}
							</div>
						</div>

						<p>{profil.beschreibung}</p>
					</div>

					{wantsToJoin && (
						<button className="btn chat-button" onClick={() => addToGroup()}>
							Zur Gruppe hinzuzufügen
						</button>
					)}
				</div>
			)}
		</>
	);
};

export default Profil;
