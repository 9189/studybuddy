import React, { useState, useEffect, useContext, useRef, useCallback } from "react";
import Filteringbar from "../../../OverlayComponents/Filteringbar/Filteringbar";
import { AuthContext } from "../../../util/AuthContext";
import InseratPreviewCard from "../InseratPreviewCard/InseratPreviewCard";
import "./InserateAnsicht.scss";

const InserateAnsicht = () => {
	// TODO: infinite scroll durch normale pagination ersetzen

	const [user] = useContext(AuthContext);

	const [inserate, setInserate] = useState([]);

	const [selectedLand, setSelectedLand] = useState(null);
	const [selectedStadt, setSelectedStadt] = useState({});
	const [laender, setLaender] = useState([]);
	const [staedteInLand, setStaedteInLand] = useState([]);
	const [startDate, setStartDate] = useState(null);
	const [endDate, setEndDate] = useState(null);

	const [loading, setLoading] = useState(true);
	const [hasMore, setHasMore] = useState(false);
	const [pageNumber, setPageNumber] = useState(0);
	const [reset, setReset] = useState(false);

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

	const fetchInserate = () => {
		setLoading(true);

		let land = "";
		let stadt = "";
		let von = "";
		let bis = "";

		if (selectedLand !== null) land += selectedLand.landId;
		if (Object.keys(selectedStadt).length !== 0 && selectedLand !== null)
			stadt += selectedStadt.stadtId;
		if (startDate !== null) von += startDate.format("DD.MM.YYYY");
		if (endDate !== null) bis += endDate.format("DD.MM.YYYY");

		const url = `http://${process.env.REACT_APP_FETCH_URL}:8080/api/inserate?page=${pageNumber}&land=${land}&stadt=${stadt}&von=${von}&bis=${bis}`;

		const ac = new AbortController();

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "GET",
			credentials: "include",
			signal: ac.signal,
		})
			.then((response) => response.json())
			.then((data) => {
				if (reset) {
					setInserate(data.content);
				} else if (inserate.length < data.totalElements) {
					setInserate((prevInserate) => {
						return [...new Set([...prevInserate, ...data.content])];
					});
				}

				setHasMore(!data.last);
				setReset(false);
				setLoading(false);
			})
			.catch((err) => {
				if (err.name === "AbortError") {
					ac.abort();
				}
			});
	};

	const search = () => {
		fetchInserate();
	};

	useEffect(() => {
		fetchLaender();
	}, [user.token]);

	useEffect(() => {
		if (selectedLand !== null) setStaedteInLand(selectedLand.staedte);
	}, [selectedLand]);

	useEffect(() => {
		fetchInserate();
	}, [user.token, pageNumber]);

	useEffect(() => {
		setReset(true);
	}, [selectedLand, selectedStadt, startDate, endDate]);

	const filteringbarProps = {
		setSelectedLand,
		setSelectedStadt,
		laender,
		staedteInLand,
		setStaedteInLand,
		startDate,
		setStartDate,
		endDate,
		setEndDate,
		search,
	};

	const observer = useRef();
	const lastInseratElementRef = useCallback(
		(node) => {
			if (loading) return;
			if (observer.current) observer.current.disconnect();
			observer.current = new IntersectionObserver((entries) => {
				if (entries[0].isIntersecting && hasMore) {
					setPageNumber((prevPageNumber) => prevPageNumber + 1);
				}
			});
			if (node) observer.current.observe(node);
		},
		[loading, hasMore]
	);

	return (
		<>
			<div className="mb-4">
				<Filteringbar {...filteringbarProps} />
			</div>

			<div className="inserate">
				{inserate.map((inserat, index) => {
					if (inserate.length === index + 1) {
						return (
							<InseratPreviewCard
								ref={lastInseratElementRef}
								key={inserat.inseratId}
								id={inserat.inseratId}
								isUsers={false}
								erstellerImageURL={
									inserat.ersteller.profilbild?.url
										? `${process.env.PUBLIC_URL}/images/${inserat.ersteller.profilbild.url}`
										: null
								}
								stadt={inserat.zielStadt ? inserat.zielStadt.name : "..."}
								land={inserat.zielLand ? inserat.zielLand.name : "..."}
								zeitraum={inserat.vonDat + " - " + inserat.bisDat}
								text={`${inserat.ersteller.name} sucht noch ${inserat.maxUser} ${
									inserat.maxUser > 1 ? "Buddys" : "Buddy"
								}`}
							/>
						);
					} else {
						return (
							<InseratPreviewCard
								key={inserat.inseratId}
								id={inserat.inseratId}
								isUsers={false}
								erstellerImageURL={
									inserat.ersteller.profilbild?.url
										? `${process.env.PUBLIC_URL}/images/${inserat.ersteller.profilbild.url}`
										: null
								}
								stadt={inserat.zielStadt ? inserat.zielStadt.name : "..."}
								land={inserat.zielLand ? inserat.zielLand.name : "..."}
								zeitraum={inserat.vonDat + " - " + inserat.bisDat}
								text={`${inserat.ersteller.name} sucht noch ${inserat.maxUser} ${
									inserat.maxUser > 1 ? "Buddys" : "Buddy"
								}`}
							/>
						);
					}
				})}
			</div>
			<p>{loading && "Loading..."}</p>
		</>
	);
};

export default InserateAnsicht;
