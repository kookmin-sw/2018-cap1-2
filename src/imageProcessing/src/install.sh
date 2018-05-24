!/bin/sh

git clone https://github.com/GoogleCloudPlatform/cloud-vision.git
chmod -R 777 cloud-vision
cd cloud-vision/python/text
sudo pip install -r requirements.txt
