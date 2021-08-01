import { useState } from "react";

const useAuth = () => {
	const [user, setUser] = useState(null);
	const [isLoading, setIsLoading] = useState(true);

	async function login(credentials, cb) {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/login";

		const response = await fetch(url, {
			headers: {
				"content-type": "application/json",
			},
			method: "POST",
			body: JSON.stringify(credentials),
			credentials: "include",
		});

		const responseValue = await response.json();

		if (response.ok) {
			setUser(responseValue);
			setIsLoading(false);
			cb();
		} else {
			setIsLoading(false);
		}
	}

	async function refresh() {
		const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/refresh";

		const response = await fetch(url, {
			headers: {
				"content-type": "application/json",
			},
			method: "GET",
			credentials: "include",
		});

		const responseValue = await response.json();

		if (response.ok) {
			setUser(responseValue);
			setTimeout(() => {
				setIsLoading(false);
			}, 300);

			setTimeout(() => {
				refresh();
			}, 899500);
		} else {
			setIsLoading(false);
		}
	}

	function logout(cb) {
		setUser(null);
		cb();
	}

	return [user, login, refresh, logout, isLoading];
};

export default useAuth;
