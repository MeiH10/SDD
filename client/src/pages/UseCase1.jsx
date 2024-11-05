import React from 'react';

const UseCase1 = () => {
    const [uid, setUid] = React.useState(undefined);
    const [error, setError] = React.useState(null);

    const handleCheck = React.useCallback(async () => {
        const response = await fetch("/api/session");
        if (!response.ok) {
            setUid(false);
            setError(response.statusText);
            return;
        } 

        const { data, error } = await response.json();
        setUid(data);
        setError(error);
    }, []);

    React.useEffect(() => {
        handleCheck();
    }, [handleCheck]);
    
    const handleLogout = React.useCallback(async () => {
        setError(null);

        const response = await fetch("/api/session", { method: "DELETE" });
        if (!response.ok) {
            setError(response.statusText);
            return;
        }

        const { good, error } = await response.json();
        if (!good) {
            setError(error);
            return;
        }

        await handleCheck();
    }, [handleCheck]);

    const handleLogin = React.useCallback(async event => {
        console.log(event);
        event.preventDefault();
        event.stopPropagation();

        setError(null);

        const response = await fetch("/api/session", {
            method: "POST",
            body: new FormData(event.target)
        });

        if (!response.ok) {
            setError(response.statusText);
            return;
        }

        const { good, error } = await response.json();
        if (!good) {
            setError(error);
            return;
        }

        await handleCheck();
    }, [handleCheck]);

    const logoutButton = <button onClick={handleLogout}>Logout</button>;

    const loginModal = <form method='POST' action='#' onSubmit={handleLogin}>
        <label>
            Email:
            <input type="email" name="email"></input>
        </label>
        <label>
            Password:
            <input type="password" name="password"></input>
        </label>
        <button>Log in</button>
    </form>;

    return <div>
        <p><b>UID</b>: {uid ?? "<none>"}</p>
        <p><b>ERROR</b>: {error}</p>
        { uid == null ? loginModal : logoutButton}
    </div>;
};

export default UseCase1;