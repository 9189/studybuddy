import React, { useEffect, useContext, useState } from "react";
import InseratPreviewCard from "../Inserat/InseratPreviewCard/InseratPreviewCard";
import { AuthContext } from "../../util/AuthContext";
import { Link } from "react-router-dom";
import "../Home/Home.scss";

const Home = () => {
	const [user] = useContext(AuthContext);

	const [meinInserat, setMeinInserat] = useState(null);
	const [gemerkteInserate, setGemerkteInserate] = useState([]);

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
				setGemerkteInserate(data);
			})
			.catch((error) => {
				console.log(error);
			});
	};

	const fetchMeinInserat = () => {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate/mine";

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
				setMeinInserat({
					id: data.inseratId,
					land: data.zielLand.name,
					stadt: data.zielStadt.name,
					von: data.vonDat,
					bis: data.bisDat,
					istAktiv: data.istAktiv,
				});
			})
			.catch((err) => console.log(err));
	};

	useEffect(() => {
		fetchGemerkteInserate();
		fetchMeinInserat();
	}, [user.token]);

	return (
		<div>
			<div className="mein-inserat">
				{meinInserat && (
					<>
						<h1 className="mt-5">Mein Inserat</h1>

						<InseratPreviewCard
							id={meinInserat?.id}
							isUsers={true}
							stadt={meinInserat?.stadt}
							land={meinInserat?.land}
							zeitraum={meinInserat?.von + " - " + meinInserat?.bis}
							text={meinInserat?.istAktiv ? "Aktiv" : "Inaktiv"}
						/>
					</>
				)}
			</div>
			<div className="merkliste mt-5">
				<h1>Merkliste</h1>

				{gemerkteInserate.length !== 0 ? (
					<div className="merkliste-inhalt">
						{gemerkteInserate.map((inserat) => (
							<InseratPreviewCard
								key={inserat?.inseratId}
								id={inserat?.inseratId}
								isUsers={false}
								erstellerImageURL={
									inserat?.ersteller.profilbild?.url
										? `${process.env.PUBLIC_URL}/images/${inserat.ersteller.profilbild.url}`
										: null
								}
								stadt={inserat.zielStadt?.name}
								land={inserat.zielLand?.name}
								zeitraum={inserat.vonDat + " - " + inserat.bisDat}
								text={`${inserat.ersteller.name} sucht noch ${inserat.maxUser} ${
									inserat.maxUser > 1 ? "Buddys" : "Buddy"
								}`}
							/>
						))}
					</div>
				) : (
					<p className="mt-5 text-center">
						Worauf wartest du? Klicke {<Link to="/suche">hier</Link>} um Inserate zu
						durchstöbern und deiner Merkliste hinzuzufügen!
					</p>
				)}
			</div>
		</div>
	);
};

export default Home;
