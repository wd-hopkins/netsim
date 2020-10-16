import librosa
import librosa.display
import numpy as np
import matplotlib.pyplot as plt


def get_tempo(file, duration):
    y, sr = librosa.load(file, duration=duration)
    onset_env = librosa.onset.onset_strength(y, sr=sr)
    return librosa.beat.tempo(onset_envelope=onset_env, sr=sr)


def show_audio_as_spectrogram(file):
    y, sr = librosa.load(file, duration=15)
    fig, ax = plt.subplots(nrows=2, ncols=1, sharex=True)
    d = librosa.amplitude_to_db(np.abs(librosa.stft(y)), ref=np.max)
    librosa.display.specshow(d, y_axis='linear', x_axis='time', sr=sr, ax=ax[0])
    ax[0].set(title='Linear-frequency power spectrogram')
    plt.show()


def waveplot(file):
    y, sr = librosa.load(file, duration=10)
    fig, ax = plt.subplots(nrows=3, sharex=True, sharey=True)
    librosa.display.waveplot(y, sr=sr, ax=ax[0])
    ax[0].set(title='Monophonic')
    ax[0].label_outer()
    plt.show()


show_audio_as_spectrogram(librosa.example("nutcracker"))
waveplot(librosa.example("nutcracker"))
