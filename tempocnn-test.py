from tempocnn.classifier import TempoClassifier
from tempocnn.feature import read_features
import numpy as np

model_name = 'cnn'
input_file = 'music/Tina_Turner_Proud_Mary.mp3'

# initialize the model (may be re-used for multiple files)
classifier = TempoClassifier(model_name)

#%% global tempo
# read the file's features
features = read_features(input_file)

# estimate the global tempo
tempo = classifier.estimate_tempo(features, interpolate=False)
print(f"Estimated global tempo: {tempo}")

#%% local tempo
# read the file's features, specify hop_length for temporal resolution
features = read_features(input_file, frames=256, hop_length=16)

# estimate local tempi, this returns tempo classes, i.e., a distribution
local_tempo_classes = classifier.estimate(features)

# find argmax per frame and convert class index to BPM value
max_predictions = np.argmax(local_tempo_classes, axis=1)
local_tempi = classifier.to_bpm(max_predictions)
print(f"Estimated local tempo classes: {local_tempi}")
