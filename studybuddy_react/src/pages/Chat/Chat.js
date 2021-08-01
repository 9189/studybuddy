import React, { useContext, useEffect, useState } from "react";
import "./Chat.scss";
import ScrollToBottom from "react-scroll-to-bottom";
import { AuthContext } from "../../util/AuthContext";
import Message from "./Message";
import { Link } from "react-router-dom";

let stompClient = null;

const Chat = () => {
	const [user] = useContext(AuthContext);
	const [contacts, setContacts] = useState([]);
	const [messages, setMessages] = useState([]);
	const [text, setText] = useState("");
	const [activeContact, setActiveContact] = useState(contacts[0]);

	useEffect(() => {
		connect();
		loadContacts();
	}, []);

	useEffect(() => {
		if (activeContact === undefined) return;

		const url = `http://${process.env.REACT_APP_FETCH_URL}:8080/messages?contact=${activeContact.id}`;

		fetch(url, {
			headers: {
				"content-type": "application/json",
				Authorization: "Bearer " + user.token,
			},
			method: "GET",
			credentials: "include",
		})
			.then((response) => response.json())
			.then((msgs) => {
				console.log(msgs);
				setMessages(
					msgs.map((msg) => {
						return {
							senderId: msg.sender.userId,
							recipientIds: msg.recipients,
							content: msg.content,
						};
					})
				);
			})
			.catch((error) => {
				console.log(error);
			});

		loadContacts();
	}, [activeContact]);

	const connect = () => {
		const Stomp = require("stompjs");
		let SockJS = require("sockjs-client");
		let socket = new SockJS("http://" + process.env.REACT_APP_FETCH_URL + ":8080/chat");

		stompClient = Stomp.over(socket);
		stompClient.connect({}, onConnected, onError);
	};

	const onConnected = () => {
		console.log("connected");
		stompClient.subscribe("/topic/user/messages/" + user.id, (msg) => onMessageReceived(msg));
	};

	const onError = (err) => {
		console.log(err);
	};

	const onMessageReceived = (msg) => {
		const message = JSON.parse(msg.body);
		loadContacts();

		setMessages((previous) => [...previous, message]);
	};

	const sendMessage = (msg) => {
		if (msg.trim() !== "") {
			const message = {
				senderId: user.id,
				recipientIds: [activeContact.id],
				content: msg,
			};
			stompClient.send("/app/chat/user/" + activeContact.id, {}, JSON.stringify(message));

			setMessages((previous) => [...previous, message]);
		}
	};

	const loadContacts = () => {
		setContacts(user.contacts);
		console.log(user.contacts);
		setActiveContact(user.contacts[0]);
	};

	return (
		<div className="outer">
			<h1 className="mt-5">Chat</h1>
			<div className="container chat-box">
				<div className="row">
					<div className="col-3 sidepanel">
						{contacts.map((contact) => (
							<div
								onClick={() => setActiveContact(contact)}
								className={
									activeContact && contact.id === activeContact.id
										? "contact active"
										: "contact"
								}
							>
								<div className="contact-wrap">
									<img id={contact.id} src={contact.profilbild} alt="" />
									<div className="meta">
										<p className="name">{contact.name}</p>
									</div>
								</div>
							</div>
						))}
					</div>

					<div className="col mycol">
						<div className="content">
							<div className="contact-profile">
								<Link to={`/profile/${activeContact && activeContact.id}`}>
									<p>{activeContact && activeContact.name}</p>
								</Link>
							</div>
							<ScrollToBottom className="messages" debug={true}>
								{messages.map(({ content, senderId }) => (
									<div className="message">
										<Message b text={content} isMine={senderId == user.id} />
									</div>
								))}
							</ScrollToBottom>
							<div className="message-input">
								<div className="wrap">
									<input
										autoComplete="off"
										name="user_input"
										size="large"
										placeholder="Write your message..."
										value={text}
										onChange={(event) => setText(event.target.value)}
										onKeyPress={(event) => {
											if (event.key === "Enter") {
												sendMessage(text);
												setText("");
											}
										}}
									/>

									<button
										onClick={() => {
											sendMessage(text);
											setText("");
										}}
									>
										<span className="icons send">send</span>
									</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	);
};

export default Chat;
