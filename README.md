# 🩺 SkinHelper

**SkinHelper** is a personal Android project I built as a student to explore how mobile machine learning can be used to help with early skin cancer detection. The app uses a TensorFlow Lite image classification model to analyze images of skin lesions and provide simple predictions based on predefined categories.

It's a learning project for me — not intended for medical use — but I’ve gained a lot from working on it and hope it can serve as a helpful reference or starting point for others interested in combining Android development with on-device machine learning.

---

## ✨ Features

- 📷 Select and crop an image of a skin lesion  
- 🧠 Run image classification using a TensorFlow Lite model  
- 🔐 Login using Firebase Authentication  
- ☁️ Store user scan history and results using Firestore  
- 🖼 View uploaded image results with Glide & uCrop  
- 🧭 Navigation with fragments and bottom nav  

---

## 🚧 Disclaimer

This app is just a demo and not intended for diagnosis or medical use. Please consult a medical professional for any concerns regarding your skin health.

---

## 🧰 Libraries Used

- **Firebase Authentication**: Handles user login and authentication (Google sign-in)  
- **Firebase Firestore**: Cloud NoSQL database for storing user classification history  
- **Firebase Storage**: Uploads and stores images in the cloud  
- **TensorFlow Lite**: On-device machine learning for image classification  
- **TensorFlow Lite GPU**: Accelerates ML inference using device GPU  
- **TensorFlow Lite Support Library**: Utility functions for preprocessing and output parsing  
- **uCrop**: Image cropping library to allow users to select and adjust images before classification  
- **Glide**: Efficient image loading and caching library  
- **Coil**: Lightweight image loading library optimized for Kotlin and Jetpack  
- **CircleImageView**: Displays user profile pictures in a circular format  

---

## 📸 Screenshots

Here are a few screenshots of the SkinHelper app in action:

## Screenshots

| Dashboard | Result |
|-----------|--------|
| <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/dashboad_empty.png?raw=true" width="250" height="500"/> | <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/dashboard_result.png?raw=true" width="250" height="500"/> |

| Learn | Details Learn |
|-------|---------------|
| <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/info.png?raw=true" width="250" height="500"/> | <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/details.png?raw=true" width="250" height="500"/> |

| History | Profile |
|---------|---------|
| <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/history.png?raw=true" width="250" height="500"/> | <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/profile.png?raw=true" width="250" height="500"/> |

| Crop | Splash Screen |
|------|---------------|
| <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/crop.png?raw=true" width="250" height="500"/> | <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/splash.png?raw=true" width="250" height="500"/> |

| Register | Login |
|----------|-------|
| <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/register.png?raw=true" width="250" height="500"/> | <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/login.png?raw=true" width="250" height="500"/> |

| Reset Password |
|----------------|
| <img src="https://github.com/methanesulfonic/Skin-Helper/blob/main/demo/reset_password.png?raw=true" width="250" height="500"/> |

---

## ▶️ Demo Video

Check out this short video demo of SkinHelper in action:

[![Watch the demo](https://upload.wikimedia.org/wikipedia/commons/e/ef/Youtube_logo.png)](https://www.youtube.com/shorts/72yxcnol74k)

---

## 🙋‍♂️ About Me

Hi! I’m a student learning Android development and machine learning. This is one of my early projects, and I built it to apply what I’ve learned about TensorFlow Lite and Firebase integration. Thanks for checking it out! 😊
