import React from "react";

const Message = ({ text, timestamp, isMine, key }) => {
  return (
    <div className={isMine ? "sent" : "reply"} key={key}>
      <p>{text}</p>
      <p>{timestamp}</p>
    </div>
  );
};

export default Message;
