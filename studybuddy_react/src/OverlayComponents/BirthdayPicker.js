import moment from "moment";
import React, { useState, useEffect } from "react";
import { SingleDatePicker, isInclusivelyBeforeDay } from "react-dates";

const BirthdayPicker = ({ setGebDat }) => {
	const [date, setDate] = useState(moment().subtract(18, "y"));
	const [focused, setFocused] = useState(false);

	useEffect(() => {
		setGebDat(date);
	}, [date, setGebDat]);

	const renderMonthElement = ({ month, onMonthSelect, onYearSelect }) => {
		let i;
		let years = [];
		for (i = moment().year() - 18; i >= moment().year() - 100; i--) {
			years.push(
				<option value={i} key={`year-${i}`}>
					{i}
				</option>
			);
		}
		return (
			<div style={{ display: "flex", justifyContent: "center" }}>
				<div>
					<select
						value={month.month()}
						onChange={(e) => onMonthSelect(month, e.target.value)}
					>
						{moment.months().map((label, value) => (
							<option value={value} key={value}>
								{label}
							</option>
						))}
					</select>
				</div>
				<div>
					<select
						value={month.year()}
						onChange={(e) => onYearSelect(month, e.target.value)}
					>
						{years}
					</select>
				</div>
			</div>
		);
	};

	return (
		<div className="mt-4">
			<SingleDatePicker
				displayFormat="DD.MM.YYYY"
				hideKeyboardShortcutsPanel={true}
				onClose={() => setFocused(false)}
				numberOfMonths={1}
				date={date}
				onDateChange={(date) => setDate(date)}
				focused={focused}
				onFocusChange={(focused) => setFocused(focused)}
				renderMonthElement={renderMonthElement}
				isOutsideRange={(day) => !isInclusivelyBeforeDay(day, moment().subtract(18, "y"))}
			/>
		</div>
	);
};

export default BirthdayPicker;
