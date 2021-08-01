import React, { useState } from "react";
import "./Filteringbar.scss";
import { DateRangePicker } from "react-dates";

const Filteringbar = ({
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
}) => {
	const [focusedInput, setFocusedInput] = useState();

	return (
		<div>
			<div className="mt-5">
				<div className="filter-bar">
					<div className="land">
						<p>Land</p>
						<select
							className="form-control"
							name="land"
							onChange={(e) => {
								if (e.target.value === "-") {
									setSelectedLand(null);
									setSelectedStadt({});
									setStaedteInLand([]);
								} else {
									setSelectedLand(laender[e.target.selectedIndex - 1]);
								}
							}}
						>
							<option defaultValue>-</option>

							{laender.map((land) => (
								<option key={land.landId}>{land.name}</option>
							))}
						</select>
					</div>
					<div className="vl"></div>
					<div className="stadt">
						<p>Stadt</p>
						<select
							className="form-control"
							name="stadt"
							onChange={(e) => {
								if (e.target.value === "-") {
									setSelectedStadt({});
								} else {
									setSelectedStadt(staedteInLand[e.target.selectedIndex - 1]);
								}
							}}
						>
							<option defaultValue>-</option>

							{staedteInLand.map((stadt) => (
								<option key={stadt.stadtId}>{stadt.name}</option>
							))}
						</select>
					</div>
					<div className="vl"></div>
					<div className="date">
						<DateRangePicker
							verticalHeight={370}
							orientation="vertical"
							numberOfMonths={1}
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
					</div>
					<button
						className="btn"
						onClick={() => {
							search();
						}}
					>
						<span className="icons">search</span>
					</button>
				</div>
			</div>
		</div>
	);
};

export default Filteringbar;
