import speech_recognition as sr
from os import path, getcwd, system
import wave
import pyaudio
from future.backports.http.client import BadStatusLine

FORMAT = pyaudio.paInt16
RATE = 44100

m = sr.Microphone()
r = sr.Recognizer()


def record_to_file(path, data):
    """
    Records from the microphone or wavdata and generates the resulting data to "path"

    :param path: output ".wav" audio file path
    :param data: input audio wavdata
    """
    p = pyaudio.PyAudio()
    sample_width = p.get_sample_size(FORMAT)
    wf = wave.open(path, 'wb')
    wf.setnchannels(1)
    wf.setsampwidth(sample_width)
    wf.setframerate(RATE)
    wf.writeframes(data)
    wf.close()

def listen_and_record(path):

    global r
    global m
    with m as source:
        try:
            r.adjust_for_ambient_noise(source)
            print("Set minimum energy threshold to {}".format(r.energy_threshold))
            print("*" * 100, "\n", "*" * 100)
            print("Start Speaking")
            data = r.listen(source, timeout=100)
            wavdata = data.get_wav_data()
            record_to_file(path, wavdata)

        except sr.WaitTimeoutError as e:
            print (e)
            print ("Listening again...")
            listen_and_record(path)


def STT(file_path, Lang="fa-IR"):

    AUDIO_FILE = path.join(path.dirname(path.realpath(__file__)), file_path)
    with sr.AudioFile(AUDIO_FILE) as source:
        audio = r.record(source)
        print ("Now recognizing...")
        if Lang == "fa-IR":
            try:
                Text = r.recognize_google(audio, language=Lang)
                print ("Recognized:  " + Text)
                return Text
            except sr.UnknownValueError:
                print("Didn't catch that.")
                data = "NONE"
                counter = 0
                while (counter < 100) & (data == "NONE"):
                    listen_and_record('speech.wav')
                    data = STT('speech.wav')
                if counter >= 100:
                    return "NONE"
                else:
                    return data
            except sr.RequestError as e:
                print("Couldn't request results from Google Speech Recognition service; {0}".format(e))
                # with open('speech.wav.txt', 'r') as f:
                #     Text = f.read()
                # print("Recognized:  " + Text)
                # return Text

            except BadStatusLine as e:
                with open('speech.wav.txt', 'r') as f:
                    Text = f.read().decode('utf-8')
                print ("Recognized:  " + Text)
                return Text
        else:
            try:
                Text = r.recognize_google(audio, language=Lang)
                print ("Recognized:  " + Text)
                return Text
            except sr.UnknownValueError:
                print("Didn't catch that.")
                data = "NONE"
                counter = 0
                # if it cannot recognize anything, it listens again for 100 times
                while (counter < 100) & (data == "NONE"):
                    listen_and_record('speech.wav')
                    data = STT('speech.wav', Lang)
                if counter >= 100:
                    return "NONE"
                else:
                    return data
            except sr.RequestError as e:
                print("Couldn't request results from Google Speech Recognition service; {0}".format(e))
                print("Recognizing the speech using PocketSphinx...")
                Text = r.recognize_sphinx(audio)
                print ("Recognized:  " + Text)
                return Text


file_path = "speech1.wav"
listen_and_record(file_path)
# for i in range(10):
STT(file_path)
