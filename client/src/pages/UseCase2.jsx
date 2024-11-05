import React from 'react';

const UseCase2 = () => {
    const [error, setError] = React.useState(null);
    const [note, setNote] = React.useState(null);
    
    const handleSelect = React.useCallback(async event => {
        console.log(event);
        event.preventDefault();
        event.stopPropagation();

        setError(null);

        const noteID = new FormData(event.target).get("id");

        const response = await fetch(`/api/note/${noteID}`, {
            method: "GET",
            body: new FormData(event.target)
        });

        if (!response.ok) {
            setError(response.statusText);
            return;
        }

        const { good, error, data } = await response.json();
        if (!good) {
            setError(error);
            return;
        }

        setNote(data);
    }, []);

    const noteSelect = <form method='POST' action='#' onSubmit={handleSelect}>
        <label>
            Note ID:
            <input type="text" name="id"></input>
        </label>
        <button>Select Note</button>
        <p><b>ERROR</b>: {error}</p>
    </form>;
    return <div>
        {noteSelect}
        <p><b>NOTE</b>: {JSON.stringify(note)}</p>
    </div>;
};

export default UseCase2;