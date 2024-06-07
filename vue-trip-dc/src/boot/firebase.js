// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
const firebaseConfig = {
  apiKey: "**key**",
  authDomain: "**authDomain**",
  projectId: "**projectId**",
  storageBucket: "**storageBucket**",
  messagingSenderId: "**messagingSenderId**",
  appId: "**appId**"
};


import { getStorage, ref } from "firebase/storage";

const storage = getStorage();

// Create a child reference
const imagesRef = ref(storage, 'images');
// imagesRef now points to 'images'

// Child references can also take paths delimited by '/'
const spaceRef = ref(storage, 'images/space.jpg');
// spaceRef now points to "images/space.jpg"
// imagesRef still points to "images"
// Initialize Firebase
const app = initializeApp(firebaseConfig);