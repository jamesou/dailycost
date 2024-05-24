from ocr_server import app
from gevent import pywsgi  
 
if __name__ == '__main__':
    server = pywsgi.WSGIServer(('0.0.0.0', 8090), app)  
    server.serve_forever()