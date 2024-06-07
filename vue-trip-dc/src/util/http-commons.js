import axios from "axios";

const VITE_VUE_API_URL = `http://${window.location.hostname}:8080/`;

// local vue api axios instance
function localAxios() {
  const instance = axios.create({
    baseURL: VITE_VUE_API_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });

  return instance;
}

function kakaoMobilityAxios() {
  const instance = axios.create({
    baseURL: "https://apis-navi.kakaomobility.com/v1/waypoints/directions",
    headers: {
      "Content-Type": "application/json;charset=utf-8",
      Authorization: "KakaoAK " + "**key**",
    },
  });

  return instance;
}

export { localAxios, kakaoMobilityAxios };
