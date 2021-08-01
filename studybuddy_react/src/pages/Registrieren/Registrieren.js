import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.css";
import CredentialForm from "./CredentialForm";
import PersonalForm from "./PersonalForm";
import { useHistory } from "react-router";

const Registrieren = () => {
	const [step, setStep] = useState(1);

	const [studienrichtungen, setStudienrichtungen] = useState([]);
	const [herkunftslaender, setHerkunftslaender] = useState([]);

	const [email, setEmail] = useState("");
	const [passwort, setPasswort] = useState("");
	const [passwortRepeat, setPasswortRepeat] = useState("");

	const [name, setName] = useState("");
	const [beschreibung, setBeschreibung] = useState("");
	const [gebDat, setGebDat] = useState();
	const [studienrichtungsId, setStudienrichtungsId] = useState(-1);
	const [herkunftsstadtId, setHerkunftsstadtId] = useState(-1);

	let history = useHistory();

	const fetchData = async () => {
		try {
			const url =
				"http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/registrieren/options";

			const response = await fetch(url, {
				headers: {
					"content-type": "application/json",
				},
				method: "GET",
				credentials: "include",
			});

			const responseValue = await response.json();

			if (response.ok) {
				console.log(responseValue);
				setStudienrichtungen(responseValue.studienrichtungen);
				setHerkunftslaender(responseValue.laender);
			}
		} catch (error) {
			console.log(error);
		}
	};

	useEffect(() => {
		fetchData();
	}, []);

	const submitData = async () => {
		const data = {
			name: name,
			email: email,
			passwort: passwort,
			beschreibung: beschreibung,
			gebDat: gebDat,
			herkunftsStadtId: herkunftsstadtId,
			studienrichtungId: studienrichtungsId,
			sprachenIds: ["de", "en"],
		};

		console.log(data);

		try {
			const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/registrieren";

			const response = await fetch(url, {
				headers: {
					"content-type": "application/json",
				},
				method: "POST",
				body: JSON.stringify(data),
				credentials: "include",
			});

			if (response.ok) {
				console.log("YAY");

				history.push("/login");
			} else {
				console.log("NOPE");
				history.push("/registrieren");
			}
		} catch (error) {
			console.log(error);
		}
	};

	const credentialProps = {
		email,
		setEmail,
		passwort,
		setPasswort,
		passwortRepeat,
		setPasswortRepeat,
		onClickNext,
	};

	const personalProps = {
		name,
		setName,
		beschreibung,
		setBeschreibung,
		studienrichtungen,
		setStudienrichtungsId,
		herkunftslaender,
		setHerkunftsstadtId,
		setGebDat,
		submitData,
		onClickBack,
	};

	const renderSwitch = () => {
		switch (step) {
			case 1:
				return <CredentialForm {...credentialProps} />;
			case 2:
				return <PersonalForm {...personalProps} />;
		}
	};

	function onClickNext() {
		setStep(step + 1);
	}

	function onClickBack() {
		setStep(step - 1);
	}

	return (
		<div>
			<h1 className="mt-5">Registrieren</h1>
			{renderSwitch()}
		</div>
	);
};

export default Registrieren;
