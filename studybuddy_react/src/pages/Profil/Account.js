import React, { useContext, useState } from "react";
import { AuthContext } from "../../util/AuthContext";

const Profil = () => {
	const [user] = useContext(AuthContext);
	const [file, setFile] = useState(null);

	const postImage = async () => {
		const formData = new FormData();
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/images";

		if (file != null) {
			formData.append("file", file, file.name);
			formData.append("email", new Blob([user.email], { type: "text/plain" }));

			fetch(url, {
				headers: {
					Authorization: "Bearer " + user.token,
				},
				processData: false,
				contentType: false,
				method: "POST",
				body: formData,
				credentials: "include",
			})
				.then((response) => console.log(response))
				.catch(console.log("Something went wrong..."));
		}
	};

	return (
		<div className="container">
			<h1 className="mt-5">Mein Account</h1>

			<p>ID: {user.id}</p>
			<p>Email: {user.email}</p>
			<p>Geburtsdatum: {user.gebDat}</p>
			<p>Herkunftsstadt: {user.herkunftsStadt.name}</p>
			<p>Studienrichtung: {user.studienrichtung.bezeichnungDeutsch}</p>

			<div className="row">
				<div className="col">
					<input
						type="file"
						className="mt-5"
						onChange={(event) => setFile(event.target.files[0])}
					/>
				</div>

				<div className="col ">
					<button className="btn btn-lg mt-5" onClick={() => postImage()}>
						Upload
					</button>
				</div>
			</div>
		</div>
	);
};

export default Profil;
