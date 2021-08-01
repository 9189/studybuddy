import React, { useState } from "react";

const Autocomplete = ({ buttonText, options, setId }) => {
	const [display, setDisplay] = useState(false);
	const [search, setSearch] = useState("");

	const update = (input) => {
		setSearch(input);
		setDisplay(false);
	};

	return (
		<div>
			{display ? (
				<div>
					<div className="container">
						<div className="row mt-4">
							<input
								id="autocomplete"
								className="col form-control"
								placeholder="Suchen..."
								value={search}
								onChange={(event) => setSearch(event.target.value)}
								autoComplete="off"
							/>

							<button
								className="col-1 btn form-control ml-2"
								onClick={() => {
									setDisplay(!display);
									setSearch("");
								}}
							>
								X
							</button>
						</div>
					</div>

					<div className="autoContainer">
						{options
							.filter(({ name }) => {
								if (
									name.substr(0, search.length).toUpperCase() ===
									search.toUpperCase()
								) {
									return true;
								}
								return false;
							})
							.slice(0, 100)
							.map((option, index) => {
								return (
									<div
										onClick={() => {
											update(option.name);
											setId(option.id);
										}}
										className="my-2"
										key={index}
										tabIndex={0}
										style={{
											backgroundColor: "#212121",
											borderRadius: "10px",
										}}
									>
										<div className="pl-2 py-1">
											<span>{option.name}</span>
										</div>
									</div>
								);
							})}
					</div>
				</div>
			) : (
				<div>
					<button
						className="btn my-4"
						onClick={() => {
							setDisplay(!display);
							setSearch("");
						}}
					>
						{buttonText}
					</button>
					<p>{search}</p>
				</div>
			)}
		</div>
	);
};

export default Autocomplete;
