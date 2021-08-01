import React, { useState, useEffect, useContext } from "react";
import "react-dates/initialize";
import "react-dates/lib/css/_datepicker.css";
import "./react_dates_overrides.scss";
import "./InseratErstellen.scss";
import { AuthContext } from "../../../util/AuthContext";
import InseratForm from "./InseratForm";
import { useHistory } from "react-router";

const InseratErstellen = () => {
	const [user] = useContext(AuthContext);

	const [focusedInput, setFocusedInput] = useState();
	const [startDate, setStartDate] = useState();
	const [endDate, setEndDate] = useState();

	const [buddyCount, setBuddyCount] = useState(0);
	const [selectedLand, setSelectedLand] = useState(null);
	const [selectedStadt, setSelectedStadt] = useState({});
	const [uni, setUni] = useState("");
	const [beschreibung, setBeschreibung] = useState("");

	const [laender, setLaender] = useState([]);
	const [staedteInLand, setStaedteInLand] = useState([]);

	let history = useHistory();

	const props = {
		focusedInput,
		setFocusedInput,
		startDate,
		setStartDate,
		endDate,
		setEndDate,
		buddyCount,
		setBuddyCount,
		setSelectedLand,
		setSelectedStadt,
		staedteInLand,
		setStaedteInLand,
		setUni,
		setBeschreibung,
		laender,
		inseratErstellen,
	};

	async function inseratErstellen() {
		let data = {
			zielStadtId: selectedStadt.stadtId,
			uni: uni,
			maxUser: buddyCount,
			beschreibung: beschreibung,
			vonDat: startDate,
			bisDat: endDate,
			erstellerId: user.id,
		};

		try {
			const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/inserate";

			const response = await fetch(url, {
				headers: {
					"content-type": "application/json",
					Authorization: "Bearer " + user.token,
				},
				method: "POST",
				body: JSON.stringify(data),
				credentials: "include",
			});

			if (response.ok) {
				console.log("YAY");
				history.push("/");
			} else {
				console.log("NOPE");
			}
		} catch (error) {
			console.log(error);
		}
	}

	useEffect(() => {
		const fetchData = async () => {
			try {
				const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/laender";

				const response = await fetch(url, {
					headers: {
						"content-type": "application/json",
						Authorization: "Bearer " + user.token,
					},
					method: "GET",
					credentials: "include",
				});

				const responseValue = await response.json();

				if (response.ok) {
					setLaender(responseValue);
				}
			} catch (error) {
				console.log(error);
			}
		};

		fetchData();
	}, [user.token]);

	useEffect(() => {
		if (selectedLand !== null) setStaedteInLand(selectedLand.staedte);
	}, [selectedLand]);

	return (
		<div>
			<h1 className="mt-5">Inserat erstellen</h1>
			<InseratForm {...props} />
		</div>
	);
};

export default InseratErstellen;
