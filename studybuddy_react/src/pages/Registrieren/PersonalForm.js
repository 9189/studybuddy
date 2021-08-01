import React, { useState } from "react";
import Autocomplete from "../../OverlayComponents/Autocomplete";
import BirthdayPicker from "../../OverlayComponents/BirthdayPicker";

const PersonalForm = ({
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
}) => {
	const getStaedtMitLaendern = () => {
		const staedte = [];

		herkunftslaender.forEach((land) => {
			land.staedte.forEach((stadt) => {
				staedte.push({
					name: `${stadt.name}, ${land.name}`,
					id: stadt.stadtId,
				});
			});
		});

		return staedte;
	};

	return (
		<div>
			<input
				type="name"
				id="name"
				name="name"
				className="form-control mt-5"
				placeholder="Name"
				pattern="^[A-Za-z0-9_.]+$"
				required
				onChange={(e) => setName(e.target.value)}
			/>

			<BirthdayPicker setGebDat={setGebDat} />

			<Autocomplete
				buttonText="Studienrichtung wählen"
				options={studienrichtungen.map((stu) => {
					return {
						id: stu.stuId,
						name: stu.bezeichnungDeutsch,
					};
				})}
				setId={setStudienrichtungsId}
			/>

			<Autocomplete
				buttonText="Herkunftsstadt wählen"
				options={getStaedtMitLaendern()}
				setId={setHerkunftsstadtId}
			/>

			<textarea
				placeholder="Beschreibung..."
				value={beschreibung}
				onChange={(event) => {
					setBeschreibung(event.target.value);
				}}
				maxLength="255"
				className="form-control mt-5"
			></textarea>

			<div className="row mt-5">
				<div className="col">
					<button className="btn btn-lg btn-info btn-block" onClick={() => onClickBack()}>
						Zurück
					</button>
				</div>

				<div className="col">
					<button className="btn btn-lg btn-info btn-block" onClick={() => submitData()}>
						Weiter
					</button>
				</div>
			</div>
		</div>
	);
};

export default PersonalForm;
