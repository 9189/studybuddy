import React from "react";

const CredentialForm = ({
  onClickNext,
  setEmail,
  email,
  passwort,
  setPasswort,
  passwortRepeat,
  setPasswortRepeat,
}) => {
  const checkEmailInUse = async () => {
    try {
      const url = "http://" + process.env.REACT_APP_FETCH_URL + ":8080/api/check?email=" + email;

      const response = await fetch(url, {
        headers: {
          "content-type": "application/json",
        },
        method: "GET",
        credentials: "include",
      });

      const responseValue = await response.json();

      if (response.ok) {
        if (responseValue) {
          console.log(responseValue);
          return true;
        } else {
          console.log(responseValue);
          return false;
        }
      } else {
        return false;
      }
    } catch (error) {
      console.log(error);
      return false;
    }
  };

  const onClick = async () => {
    if (passwort !== passwortRepeat) {
      alert("Passwörter müssen übereinstimmen!");
    } else if (await checkEmailInUse(email)) {
      alert("Email bereits in verwendung");
    } else {
      onClickNext();
    }
  };

  return (
    <div>
      <input
        value={email}
        type="email"
        id="email"
        name="email"
        className="form-control my-4"
        placeholder="E-mail"
        pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,}$"
        required
        onChange={(e) => {
          setEmail(e.target.value);
        }}
      />
      <input
        value={passwort}
        type="password"
        id="pwd"
        name="pwd"
        className="form-control my-4"
        placeholder="Passwort"
        pattern=".{8,}"
        maxLength="56"
        required
        onChange={(e) => {
          setPasswort(e.target.value);
        }}
      />
      <input
        value={passwortRepeat}
        type="password"
        name="pwd-repeat"
        className="form-control my-4"
        placeholder="Passwort wiederholen"
        required
        onChange={(e) => {
          setPasswortRepeat(e.target.value);
        }}
      />

      <button
        className="btn btn-lg btn-info btn-block mt-5"
        onClick={() => onClick()}
      >
        Weiter
      </button>
    </div>
  );
};

export default CredentialForm;
