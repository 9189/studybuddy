import React, { useContext } from "react";
import { Redirect, Route } from "react-router-dom";
import { AuthContext } from "./AuthContext";
import { ReactComponent as Spinner } from "../media/spinner.svg";

const PrivateRoute = ({ children, ...rest }) => {
	const [user, , , , isLoading] = useContext(AuthContext);

	if (isLoading) {
		return <Spinner className="spinner-full" />;
	}

	return (
		<Route
			{...rest}
			render={({ location }) => {
				if (user !== null) {
					return children;
				} else {
					return (
						<Redirect
							to={{
								pathname: "/login",
								state: { from: location },
							}}
						/>
					);
				}
			}}
		/>
	);
};

export default PrivateRoute;
