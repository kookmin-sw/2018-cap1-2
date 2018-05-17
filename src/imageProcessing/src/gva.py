from sys import argv
from os import makedirs
import os
from os.path import join, basename
import json
import requests
from base64 import b64encode
import cv2

from google.cloud import vision as GVA
from google.cloud.vision import types

#GVA.text_detection()

APPKEY = 
ENDPOINT_URL = "https://vision.googleapis.com/v1/images:annotate"
RESULTS_DIR = "jsons"
makedirs(RESULTS_DIR,exist_ok=True)
ROOT = "/root/PycharmProjects/2018-cap1-2/src/imageProcessing/chars"
result = open("../result/result.txt",'w')
pixel = 5

def make_image_data_list():
    """
    image_filenames is a list of filename strings
    Returns a list of dicts formatted as the Vision API
        needs them to be
    """
    #print(folder)
    img_requests = []
    imgPath = ROOT #+ str(folder[0])
    imgs = os.listdir(imgPath)
    imgs.sort()

    for imgname in imgs:
        with open(imgPath + "/" +imgname, 'rb') as f:
                 ctxt = b64encode(f.read()).decode()
                 img_requests.append({
                     'image': {'content': ctxt},
                     'features': [{
                            'type': 'TEXT_DETECTION',
                           'maxResults': 1
                        }]
             })
    print("make_image_data_list")
    return img_requests

def make_image_data():
    """Returns the image data lists as bytes"""
    imgdict = make_image_data_list()
    print("make_image_data")
    return json.dumps({"requests": imgdict }).encode()


def request_ocr(api_key):
    response = requests.post(ENDPOINT_URL,
                             data=make_image_data(),
                             params={'key': api_key},
                             headers={'Content-Type': 'application/json'})
    print("request_ocr")
    return response


if __name__ == '__main__':
    api_key= APPKEY
    folder = ROOT
    if not api_key or not folder:
        print("""
            Please supply an api key, then one or more image filenames
            $ python cloudvisreq.py api_key image1.jpg image2.png""")
    else:
        response = request_ocr(api_key)
        if response.status_code != 200 or response.json().get('error'):
            print(response.text)
        else:
            for idx, resp in enumerate(response.json()['responses']):
                # save to JSON file
                imgname = folder[idx]
                jpath = join(RESULTS_DIR, basename(imgname) + '.json')
                with open(jpath, 'w') as f:
                    datatxt = json.dumps(resp, indent=2)
                    print("Wrote", len(datatxt), "bytes to", jpath)
                    f.write(datatxt)

                # print the plaintext to screen for convenience
                print("---------------------------------------------")
                t = resp['textAnnotations'][0]
                print("    Bounding Polygon:")
                print(t['boundingPoly'])
                print("    Text:")
                print(t['description'])
               # result.write("---------------" + str(pixel) + " pixel\n")
                result.write("\n")
                ss = t['description']
                result.write(t['description'])
                result.write(str(len(ss))+'\n')
                pixel = pixel + 1