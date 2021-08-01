import React from "react";
import { useHistory } from "react-router";
import account_circle from "../../media/account_circle.svg";
import "./ProfilPreview.scss";

const ProfilPreview = ({
	id,
	profilbildURL,
	name,
	alter,
	studium,
	istErsteller,
	contactZeigen,
}) => {
	const history = useHistory();

	return (
		<div
			className="profil-card my-3"
			onClick={() => {
				history.push(`/profile/${id}`);
			}}
		>
			<div className="row flex-nowrap">
				<div className="col profilbild-wrapper align-self-center">
					<img src={profilbildURL || account_circle} alt="bla" />
				</div>

				<div className="col-7 text pt-2">
					<h3>
						{name}, {alter}
					</h3>

					<h4>Studiert {studium}</h4>

					<p>{istErsteller ? "Ersteller" : "Buddy"}</p>
				</div>

				<div className="col chat-button align-self-center">
					{false && <span className="icons">chat_bubble_outline</span>}
				</div>
			</div>
		</div>
	);
};

export default ProfilPreview;
