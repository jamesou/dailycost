import os
import cv2
import time
import yaml
import uuid
import json
from datetime import timedelta
from flask import Flask, render_template, request, jsonify
from werkzeug.utils import secure_filename
from paddleocr import PPStructure
import paksave_receipt_parser
# import logging
# logging.disable(logging.DEBUG)   
# logging.disable(logging.WARNING)

def allowed_file(fname):
    return '.' in fname and fname.rsplit('.', 1)[1].lower() in ['png', 'jpg', 'jpeg']

app = Flask(__name__)
app.config['JSON_AS_ASCII'] = False
app.config['SEND_FILE_MAX_AGE_DEFAULT'] = timedelta(hours=1)
app.config['MAX_CONTENT_LENGTH'] = 16 * 1024 * 1024

@app.route("/")
def index():
    return render_template('index.html')
    
@app.route('/ocr', methods=['POST', 'GET'])
def detect():
    file = request.files['file']
    if file and allowed_file(file.filename):
        ext = file.filename.rsplit('.', 1)[1]
        random_name = '{}.{}'.format(uuid.uuid4().hex, ext)
        savepath = os.path.join('caches', secure_filename(random_name))
        file.save(savepath)
        # time-1
        t1 = time.time()
        # save_folder = './'
        # img_path = 'Image_20240417151759.jpg'
        # # img_path = 'Image_20240417154200.jpg'
        # # img_path = 'Image_20240419145953.jpg'
        # # img_path = 'Image_20240419150034.jpg'
        img = cv2.imread(savepath)
        result = table_engine(img)
        structured_data = paksave_receipt_parser.parse_result(result=result)
        # time-2
        t2 = time.time()
        return jsonify({
            'status': 'success',
            'reg_res': structured_data,
            'time_consuming': '{:.4f}s'.format(t2-t1)
        })
    return jsonify({'status': 'faild'})

if __name__ == '__main__':
    table_engine = PPStructure()
    app.run(host='0.0.0.0', port=8090, debug=True, threaded=False, processes=1)