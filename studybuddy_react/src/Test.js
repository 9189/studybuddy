import React, { useContext, useEffect, useState } from "react";
import Filteringbar from "./OverlayComponents/Filteringbar/Filteringbar.js";
import { AuthContext } from "./util/AuthContext";

const Test = () => {
	const [user] = useContext(AuthContext);

	const [selectedLand, setSelectedLand] = useState(null);
	const [selectedStadt, setSelectedStadt] = useState({});
	const [laender, setLaender] = useState([]);
	const [staedteInLand, setStaedteInLand] = useState([]);
	const [startDate, setStartDate] = useState();
	const [endDate, setEndDate] = useState();

	const props = {
		setSelectedLand,
		setSelectedStadt,
		laender,
		staedteInLand,
		setStaedteInLand,
		startDate,
		setStartDate,
		endDate,
		setEndDate,
	};

	const fetchLaender = async () => {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/laender";

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "GET",
			credentials: "include",
		})
			.then((response) => response.json())
			.then((data) => setLaender(data))
			.catch((error) => console.error(error));
	};

	useEffect(() => {
		fetchLaender();
	}, [user.token]);

	useEffect(() => {
		if (selectedLand !== null) setStaedteInLand(selectedLand.staedte);
	}, [selectedLand]);

	return (
		<>
			<Filteringbar {...props} />
		</>
	);
};

export default Test;
