import React, { useEffect } from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import Registrieren from "./pages/Registrieren/Registrieren";
import Login from "./pages/Login/Login";
import PrivateRoute from "./util/PrivateRoute";
import useAuth from "./util/useAuth";
import { AuthContext } from "./util/AuthContext";
import Home from "./pages/Home/Home";
import InseratErstellen from "./pages/Inserat/InseratErstellen/InseratErstellen";
import InserateAnsicht from "./pages/Inserat/InserateAnsicht/InserateAnsicht";
import Chat from "./pages/Chat/Chat";
import Account from "./pages/Profil/Account";

import Test from "../src/Test";

import "./App.css";
import NavPage from "./pages/Navigation/NavPage";
import InseratDetail from "./pages/Inserat/InseratDetail/InseratDetail";
import Profil from "./pages/Profil/Profil";

const App = () => {
	let auth = useAuth();

	useEffect(() => {
		const [, , refresh] = auth;
		refresh();
	}, []);

	return (
		<div className="container">
			<AuthContext.Provider value={auth}>
				<Router>
					<Switch>
						<PrivateRoute exact path="/">
							<NavPage>
								<Home />
							</NavPage>
						</PrivateRoute>

						<PrivateRoute path="/suche">
							<NavPage>
								<InserateAnsicht />
							</NavPage>
						</PrivateRoute>

						<PrivateRoute path="/add">
							<NavPage>
								<InseratErstellen />
							</NavPage>
						</PrivateRoute>

						<PrivateRoute path="/chat">
							<NavPage>
								<Chat />
							</NavPage>
						</PrivateRoute>

						<PrivateRoute path="/profil">
							<NavPage>
								<Account />
							</NavPage>
						</PrivateRoute>

						<PrivateRoute exact path="/inserate/:id">
							<NavPage>
								<InseratDetail />
							</NavPage>
						</PrivateRoute>

						<PrivateRoute exact path="/profile/:id">
							<NavPage>
								<Profil />
							</NavPage>
						</PrivateRoute>

						<Route path="/registrieren">
							<Registrieren />
						</Route>

						<Route path="/login">
							<Login />
						</Route>

						<PrivateRoute path="/test">
							<Test />
						</PrivateRoute>
					</Switch>
				</Router>
			</AuthContext.Provider>
		</div>
	);
};

export default App;
