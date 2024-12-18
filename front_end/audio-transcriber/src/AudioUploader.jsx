import { useState } from "react";
import axios from "axios";

const AudioUploader = () => {
  const [file, setFile] = useState(null);
  const [transcription, setTranscription] = useState("");

  const handleFileChange = (e) => {
    setFile(e.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      alert("Please upload an audio file before submitting.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await axios.post(
        "http://localhost:8080/api/transcribe", // Correct endpoint
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data", // Fixed "headers" capitalization
          },
        }
      );
      setTranscription(response.data); // Set the transcription response
    } catch (error) {
      console.error("Error transcribing file: ", error);
      alert("There was an error transcribing the file. Please try again.");
    }
  };

  return (
    <div className="container">
      <h1>Audio to Text Transcriber</h1>
      <div className="file-input">
        <input type="file" accept="audio/*" onChange={handleFileChange} />
      </div>
      <button className="upload-button" onClick={handleUpload}>
        Upload
      </button>
      <div className="transcription-result">
        <h2>Transcription Result</h2>
        <p>{transcription}</p>
      </div>
    </div>
  );
};

export default AudioUploader;
