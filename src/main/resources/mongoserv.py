#!/usr/bin/python3
import docker
from flask import Flask, request, jsonify
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps

app = Flask(__name__)
api = Api(app)

class Mongos(Resource):

    def get(self):
        client = docker.from_env()
        return {'mongos': [c.name for c in client.containers.list()]}

    def post(self):
        r = request.json
        nm = r['instId']
        port = r['port']
        print(nm, port)
        client = docker.from_env()
        d = {}
        d['27017/tcp'] = port
        d['27018/tcp'] = port + 1
        d['27019/tcp'] = port + 2
        cont=client.containers.run('mongo', detach=True, ports=d, name=nm)
        print(cont)
        request.json['connection'] = 'mongodb://192.168.1.134:' + str(port)
        request.json['success'] = True
        return request.json, 201

    def delete(self, inst):
        print(inst)
        client = docker.from_env()
        cont = client.containers.get(inst)
        cont.kill()


api.add_resource(Mongos, '/mongos') # Route_1
api.add_resource(Mongos, '/mongos/<inst>', endpoint = 'mongo') # Route_3

if __name__ == '__main__':
    app.run(host='0.0.0.0', port='5002')
