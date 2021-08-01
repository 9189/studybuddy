import React, { forwardRef } from "react";
import "./InseratPreviewCard.scss";
import city_icon from "../../../media/city.png";
import account_circle from "../../../media/account_circle.svg";
import { useHistory } from "react-router";

const InseratPreviewCard = forwardRef(
	({ id, isUsers, imageURL, stadt, land, zeitraum, text, erstellerImageURL }, ref) => {
		const history = useHistory();

		if (isUsers) {
			return (
				<div
					ref={ref}
					className="inserat-card mine mb-4"
					onClick={() => {
						history.push(`/inserate/${id}`);
					}}
				>
					<div className="box">
						<div className="row">
							<div className="col">
								<div className="image-cropper">
									<img src={imageURL || city_icon} alt="stadt" />
								</div>
							</div>
							<div className="col-xs-9 mt-2">
								<h3>
									{stadt}, {land}
								</h3>
								<h4>{zeitraum}</h4>
							</div>
						</div>
					</div>
				</div>
			);
		} else {
			return (
				<div
					ref={ref}
					className="inserat-card mb-4"
					onClick={() => {
						history.push(`/inserate/${id}`);
					}}
				>
					<div className="container box">
						<div className="row ">
							<div className="col-xs-3">
								<div className="image-cropper">
									<img src={imageURL || city_icon} alt="" />
								</div>
							</div>
							<div className="col-xs title">
								<h3>
									{stadt}, {land}
								</h3>
								<h4>{zeitraum}</h4>
							</div>
						</div>

						<div className="row float-right user-sucht">
							<div className="col-xs-9 pt-4">
								<p>{text}</p>
							</div>
							<div className="col">
								<div
									className={
										erstellerImageURL == null
											? "account-avatar"
											: "profile-image-cropper"
									}
								>
									<img src={erstellerImageURL || account_circle} alt="" />
								</div>
							</div>
						</div>
					</div>
				</div>
			);
		}
	}
);

export default InseratPreviewCard;
