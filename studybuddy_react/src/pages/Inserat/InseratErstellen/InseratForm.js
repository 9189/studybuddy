import React from "react";
import { DateRangePicker } from "react-dates";

const InseratForm = ({
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
}) => {
	return (
		<div>
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
			</div>

			<textarea
				maxLength="255"
				className="form-control mt-5"
				onChange={(e) => setBeschreibung(e.target.value)}
				placeholder="Beschreibung..."
				required
			></textarea>

			<button
				className="btn btn-lg btn-info btn-block mt-5"
				onClick={() => {
					inseratErstellen();
				}}
			>
				Erstellen
			</button>
		</div>
	);
};

export default InseratForm;
