import React, { useContext, useState, useEffect } from "react";
import { AuthContext } from "../../util/AuthContext";
import { useHistory, useLocation } from "react-router-dom";

const Login = () => {
	const [email, setEmail] = useState("");
	const [passwort, setPasswort] = useState("");

	const [user, login] = useContext(AuthContext);

	let data = {
		email: email,
		passwort: passwort,
	};

	let history = useHistory();
	let location = useLocation();

	let { from } = location.state || { from: { pathname: "/" } };

	useEffect(() => {
		if (user !== null) history.replace(from);
	}, [user, from, history]);

	return (
		<div>
			<h1 className="mt-5">Login</h1>

			<input
				name="email"
				type="email"
				value={email}
				onChange={(e) => setEmail(e.target.value)}
				className="form-control my-4"
				placeholder="Name"
				pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
				required
			/>

			<input
				name="passwort"
				type="password"
				value={passwort}
				onChange={(e) => setPasswort(e.target.value)}
				className="form-control my-4"
				placeholder="Passwort"
				pattern=".{8,}"
				maxLength="56"
				required
			/>

			<button
				className="btn btn-lg btn-info btn-block mt-5"
				type="submit"
				name="signup-submit"
				onClick={() => {
					login(data, () => {
						history.replace(from);
					});
				}}
			>
				Login
			</button>
		</div>
	);
};

export default Login;
