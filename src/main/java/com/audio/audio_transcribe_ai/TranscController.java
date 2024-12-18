package com.audio.audio_transcribe_ai;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.audio.transcription.AudioTranscriptionResponse;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("api/transcribe")
public class TranscController {
    private final OpenAiAudioTranscriptionModel transcriptionModel;

    public TranscController(@Value("${spring.ai.openai.api-key}") String apiKey) {
        System.out.println("*********** "+ apiKey);
//        OpenAiAudioApi openAiAudioApi = new OpenAiAudioApi(System.getenv(apiKey));
//        OpenAiAudioApi openAiAudioApi = new OpenAiAudioApi(System.getenv(apiKey));
        OpenAiAudioApi openAiAudioApi = new OpenAiAudioApi(apiKey);

        this.transcriptionModel = new OpenAiAudioTranscriptionModel(openAiAudioApi);

    }
//      @PostMapping
//              public ResponseEntity<String> transcribeAudio(
//                      @RequestParam("file")MultipartFile file) throws IOException {
//          File tempFile = File.createTempFile("audio",".wav");
//          file.transferTo(tempFile);
//
//          OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
//                  .withLanguage("en")
//                  .withTemperature(0f)
//                  .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
//                  .build();
//          FileSystemResource audioFile = new FileSystemResource(tempFile);
//
//          AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile, transcriptionOptions);
//          AudioTranscriptionResponse response = transcriptionModel.call(transcriptionRequest);
//
//          tempFile.delete();
//          return new ResponseEntity<>(response.getResult().getOutput(), HttpStatus.OK);
//
//
//        }
@PostMapping
public ResponseEntity<String> transcribeAudio(@RequestParam("file") MultipartFile file) {
    File tempFile = null;
    try {
        tempFile = File.createTempFile("audio", ".wav");
        file.transferTo(tempFile);

        OpenAiAudioTranscriptionOptions transcriptionOptions = OpenAiAudioTranscriptionOptions.builder()
                .withLanguage("en")
                .withTemperature(0f)
                .withResponseFormat(OpenAiAudioApi.TranscriptResponseFormat.TEXT)
                .build();
        FileSystemResource audioFile = new FileSystemResource(tempFile);

        AudioTranscriptionPrompt transcriptionRequest = new AudioTranscriptionPrompt(audioFile, transcriptionOptions);
        AudioTranscriptionResponse response = transcriptionModel.call(transcriptionRequest);

        return new ResponseEntity<>(response.getResult().getOutput(), HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>("Failed to process the audio file.", HttpStatus.INTERNAL_SERVER_ERROR);
    } finally {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }
}



}
