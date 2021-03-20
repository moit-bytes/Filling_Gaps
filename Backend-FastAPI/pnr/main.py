from fastapi import FastAPI
from typing import Optional
from pydantic import BaseModel
import requests
from bs4 import BeautifulSoup


app = FastAPI()


@app.get('/')
def index():
    return {"name": "yuvaraj"}


@app.post('/pnr')
def pnr(pnr: str):
    print(pnr)

    def bkng(soup, d):
        chart = soup.find(class_="chart-status-txt")
        chart = chart.text
        e = soup.find(class_="boarding-detls")
        e1 = ((e.text.replace("\n", " ")))
        e1 = e1.replace("DAY OF BOARDING", '')
        e1 = e1.replace("CLASS", '')
        a = e1.split("  ")
        d['platform'] = a[-2].split()[-1]
        d['date_of_jrny'] = a[1]
        d["class"] = a[-3]
        d["status"] = chart
        return d

    def srcdest(soup, d):

        f = soup.find(class_="train-route")
        a = []
        for i in f.findAll('div', attrs={'class': 'col-xs-4'}):
            l = i.text.replace("\n", " ")
            l = l.replace("FROM", " ")
            l = l.replace("TO", " ")
            a.append(l)
        e = soup.find_all(class_="pnr-bold-txt")
        l1 = []
        for i in e:
            i = i.text
            try:
                i = i.replace("\n", "")
                i = i.replace("  ", "")
                l1.append(i)
            except:
                l1.append(i)
        a1 = a[0].split("|")
        d['src'] = a1[0].replace(" ", "")
        cd = a1[1].split()
        d['src_code'] = cd[0]
        d['start_time'] = cd[1]+" "+cd[2]
        a1 = a[1].split("|")
        d['dst'] = a1[0].replace(" ", "")
        cd = a1[1].split()
        d['dst_code'] = cd[0]
        d['dest_time'] = cd[1]+" "+cd[2]
        d['valid'] = True
        d["train_name"] = l1[2][5:]
        return d

    d = {'valid': False, 'src': "", 'dst': "", 'src_code': "",
         'dst_code': "", "start_time": "", "dest_time": "", 'platform': "", 'date_of_jrny': "", "status": "", "class": "", "train_name": ""}

    if len(pnr) > 10 or len(pnr) < 10:
        print(d)
        return {'valid': d}
    try:
        url = 'http://www.railyatri.in/pnr-status/' + pnr
        html = requests.get(url)
        soup = BeautifulSoup(html.content, 'html.parser')
        srcdest(soup, d)
        bkng(soup, d)
        print(d)
        return {'valid': d}
    except:
        print(d)
        return {'valid': d}


@app.post('/station')
def station(pnr: str):
    if len(pnr) > 10 or len(pnr) < 10:

        return {'valid': False}
    try:
        print(pnr)
        url = 'http://www.railyatri.in/pnr-status/' + pnr
        html = requests.get(url)
        soup = BeautifulSoup(html.content, 'html.parser')
        e = soup.find_all(class_="pnr-bold-txt")
        l = []
        for i in e:
            i = i.text
            try:
                i = i.replace("\n", "")
                i = i.replace("  ", "")
                l.append(i)
            except:
                l.append(i)
        num = (l[2][:5])
        print(num)
        from datetime import date
        today = date.today()
        d = today.strftime("%Y%m%d")
        a = "http://indianrailapi.com/api/v2/livetrainstatus/apikey/b9bee0dfe18061e0a9c7b67630e83fbe/TrainNumber/" + \
            str(num)+'/date/'+str(d)+'/'
        dk = requests.get(a)
        print(dk)
        result = dk.json()
        print(result)
        try:
            a = result["TrainRoute"]
            l = []
            for i in a:
                l.append(i['StationName']+" "+i['StationCode'])
            return {'valid': True, 'output': l}
        except:
            return {'valid': False}
    except:

        return {'valid': False}
