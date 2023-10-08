const baseUrl = 'http://localhost:8080/';

export async function getApi(endpoint) {
    let url = baseUrl + endpoint;
    const response = await fetch(url);
    const body = await response.json();
    if (response.status !== 200) throw Error(body.message);
    return body;
}

export async function postApi(endpoint, data) {
    let url = baseUrl + endpoint;
    const response = await fetch(url, {
        method: 'POST',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(data)
    });
    const body = await response.json();
    if (response.status !== 200) throw Error(body.message);
    return body;
}

export async function putApi(endpoint, data) {
    let url = baseUrl + endpoint;
    const response = await fetch(url, {
        method: 'PUT',
        mode: 'cors',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        redirect: 'follow',
        referrerPolicy: 'no-referrer',
        body: JSON.stringify(data)
    });
    const body = await response.text();
    if (200 > response.status >= 300) throw Error(JSON.parse(body).message);
    return true;
}