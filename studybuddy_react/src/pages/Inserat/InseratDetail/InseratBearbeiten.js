import React, { useState, useEffect, useContext } from "react";
import city_icon from "../../../media/city.png";
import { DateRangePicker } from "react-dates";
import { AuthContext } from "../../../util/AuthContext";
import "./InseratDetail.scss";

const InseratBearbeiten = ({ inserat, inseratSpeichern, ersteller, deleteInserat }) => {
	const [user] = useContext(AuthContext);

	const [focusedInput, setFocusedInput] = useState();
	const [startDate, setStartDate] = useState();
	const [endDate, setEndDate] = useState();

	const [selectedLand, setSelectedLand] = useState(null);
	const [selectedStadt, setSelectedStadt] = useState({});
	const [beschreibung, setBeschreibung] = useState(inserat?.beschreibung);
	const [buddyCount, setBuddyCount] = useState(inserat?.maxUser);
	const [uni, setUni] = useState(inserat?.uni);

	const [laender, setLaender] = useState([]);
	const [staedteInLand, setStaedteInLand] = useState([]);

	useEffect(() => {
		console.log(inserat);

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
		<div className="mt-5">
			<div className="image-container">
				<div className="image-wrapper">
					<img src={city_icon} alt="city" />
				</div>
			</div>

			<select
				className="form-control my-4"
				name="land"
				onChange={(e) => {
					if (e.target.value === "- Land -") {
						setSelectedLand(null);
						setSelectedStadt({});
						setStaedteInLand([]);
					} else {
						setSelectedLand(laender[e.target.selectedIndex - 1]);
					}
				}}
			>
				<option defaultValue>- Land -</option>

				{laender.map((land) => (
					<option key={land.landId}>{land.name}</option>
				))}
			</select>

			<select
				className="form-control my-4"
				name="stadt"
				onChange={(e) => {
					if (e.target.value === "- Stadt -") {
						setSelectedStadt({});
					} else {
						setSelectedStadt(staedteInLand[e.target.selectedIndex - 1]);
					}
				}}
			>
				<option defaultValue>- Stadt -</option>

				{staedteInLand.map((stadt) => (
					<option key={stadt.stadtId}>{stadt.name}</option>
				))}
			</select>

			<input
				value={uni}
				className="form-control my-4"
				name="uni"
				onChange={(e) => setUni(e.target.value)}
				placeholder="Uni"
				required
			/>

			<DateRangePicker
				hideKeyboardShortcutsPanel={true}
				displayFormat="DD.MM.YYYY"
				startDate={startDate}
				startDateId="start-date"
				endDate={endDate}
				endDateId="end-date"
				onDatesChange={({ startDate, endDate }) => {
					setStartDate(startDate);
					setEndDate(endDate);
				}}
				focusedInput={focusedInput}
				onFocusChange={(focusedInput) => setFocusedInput(focusedInput)}
				block
			/>

			<div className="mt-5 row">
				<div className="col">
					<p>Anzahl an Buddys</p>
				</div>

				<div className="col">
					<button
						className="btn btn-lg btn-info btn-block"
						onClick={() => {
							if (buddyCount > 0) {
								setBuddyCount(buddyCount - 1);
							}
						}}
					>
						-
					</button>
				</div>

				<div className="col text-center">
					<p>{buddyCount}</p>
				</div>

				<div className="col">
					<button
						className="btn btn-lg btn-info btn-block"
						onClick={() => {
							setBuddyCount(buddyCount + 1);
						}}
					>
						+
					</button>
				</div>

				<textarea
					value={beschreibung}
					maxLength="255"
					className="form-control mt-5"
					onChange={(e) => setBeschreibung(e.target.value)}
					placeholder="Beschreibung..."
					required
				></textarea>
			</div>

			{ersteller.userId == user.id && (
				<div className="user-controls">
					<p
						onClick={() =>
							inseratSpeichern({
								zielStadtId: selectedStadt.stadtId,
								uni: uni,
								maxUser: buddyCount,
								beschreibung: beschreibung,
								vonDat: startDate,
								bisDat: endDate,
								erstellerId: user.id,
							})
						}
					>
						Speichern
					</p>
					<p onClick={() => deleteInserat()}>Löschen</p>
				</div>
			)}
		</div>
	);
};

export default InseratBearbeiten;
