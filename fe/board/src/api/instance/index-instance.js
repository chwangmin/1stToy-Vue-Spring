import axios from "axios";

const APP_BASE_URI = "http://10.0.2.22:8021/";

function create(url, options = {}) {
    const instance = axios.create(Object.assign({ baseUrl: url }, options));
    return instance;
}

const board = create(`${APP_BASE_URI}/board`);

export { board };
