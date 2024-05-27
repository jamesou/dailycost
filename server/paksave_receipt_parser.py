from bs4 import BeautifulSoup
import logging
 
logger = logging.getLogger('paksave')

def is_numeric(s):
    return s.isdigit() or s.isnumeric() or is_float(s)

def is_float(s):
    try:
        float(s)
        return True
    except ValueError:
        return False
#10 types of document regions：Table, Figure, Figure caption, Table, Table caption, Header, Footer, Reference, Equation)
def parse_result(result):
    #4 elements grouped one row，qty is number, amount with $    
    type = result[0]['type']
    resultObj = result[0]['res']
    print(f"type:{type}")
    if type == 'figure':
        return parse_figure(resultObj)
    elif type == 'table':
        return parse_table(resultObj.get('html'))
    else:
        return []

     
def parse_table(html):
    print(f"html:{html}")
    soup = BeautifulSoup(html, 'lxml')
    table = soup.find('table')
    filtered_list=[]
    thead = table.find('thead')
    if thead:
        header = [td.get_text(strip=True) for td in thead.find_all('td')]
        filtered_list.append(header)
    tbody = table.find('tbody')
    if tbody:
        rows = tbody.find_all('tr')

    for row in rows:
        columns = row.find_all('td')
        row_data = [col.get_text(strip=True) for col in columns]
        filtered_list.append(row_data)
    grouped_list=[]
    count = 1
    num = len(filtered_list)
    for row in filtered_list:
        print(f"row:{row}")
        item_len = len(row)
        item_info={}
        item_info['item_name'] = row[0]
        if item_len==3:
            item_qty_array=row[1].strip('@').split(" ")
            item_info['item_qty']=item_qty_array[0].strip('@')
            if len(item_qty_array)>1:
                if 'EA' not in item_qty_array[1]:
                    item_info['item_amount_unit']=item_qty_array[1]+"EA="
                else:
                    item_info['item_amount_unit']=item_qty_array[1] 
            item_amount_array = row[2].strip('*').split(" ")
            item_info['item_amount']=item_amount_array[0].strip('*')
            if len(item_amount_array)>1:
                # print(f"item_amount_array[0]:{item_amount_array[0]}")
                # print(f"item_amount_array[1]:{item_amount_array[1]}")
                if 'EA' in item_amount_array[0] or '=' in item_amount_array[0]:
                    item_info['item_amount_unit']=item_amount_array[0]
                    item_info['item_amount']=item_amount_array[1].strip('*')
                if 'EA' in item_amount_array[1] or '=' in item_amount_array[1]:
                    item_info['item_amount_unit']=item_amount_array[1]
        if item_len==4:
            item_info['item_qty']=row[1].strip('@')
            item_amount_unit_array = row[2].split(" ")
            item_info['item_amount_unit'] = item_amount_unit_array[0]
            if row[3]=='' and len(item_amount_unit_array)>1:
                item_info['item_amount'] = item_amount_unit_array[1]
            else:
                item_info['item_amount']=row[3].strip('*')
        if item_info['item_amount'] == '':
            if item_info.get('item_qty'):
                item_info['item_amount'] = item_info['item_qty'].split("E")[0]
            if item_info.get('item_amount_unit'):
                item_info['item_amount'] = item_info['item_amount_unit'].split("E")[0]
        if num == count:
            col_num = len(row)
            if col_num == 1:
                item_info['item_amount']= row[0]
            elif col_num>1:
                item_info['item_amount']= row[1]
        print(f"item_info:{item_info}")
        grouped_list.append(item_info)
        count=count+1
    return grouped_list

def parse_figure(res_list):
    grouped_list=[]
    print(f"res_list:{res_list}")
    res_list_filtered = []
    confidence_rate=0.75
    for item in res_list: #filter low confidence data
        confidence = item.get('confidence')
        text = item.get('text')
        if confidence>confidence_rate:
            if text is not None and text!='@':
                res_list_filtered.append(text)
        else:
            print(f"confidence {confidence} is less than {confidence_rate}, text:{text}")
    # print(f"res_list_filtered:{res_list_filtered}")       
    structured_list = []
    for text in res_list_filtered:
        # logger.info(f"type(item): {type(item)}")
        temp = None
        if text.find("@") != -1:
            print(f"find '@' keyword remove the @")
            temp = text.strip('@')
        if text.find("*") != -1:
            print(f"find '*' keyword remove the *")
            temp = text.strip('*')
        if temp is not None:
            structured_list.append(temp)
        else:
            structured_list.append(text)
    group_size = 4
    item_info = {}
    print(f"structured_list:{structured_list}")
    count=1
    num = len(structured_list)
    for text in structured_list:
        print(f"text:{text},count:{count}") #for debug
        if is_numeric(text):
            print(f"is_numeric:{text}")
            item_info['item_qty']=text
        elif '$'in text and '=' in text:
            print(f"item_amount_unit_1:{text}")
            item_info['item_amount_unit']=text
        elif '$'in text and 'EA' in text:
            print(f"item_amount_unit_2:{text}")
            item_info['item_amount_unit']=text
        elif '$'in text and 'EH' in text:
            print(f"item_amount_unit_3:{text}")
            item_info['item_amount_unit']=text
        elif '$'in text and '=' not in text \
            and is_numeric(text.strip('$')) \
            or is_numeric(text.strip('-$')):
            print(f"item_amount:{text}")
            item_info['item_amount']=text
        else:
            print(f"item_name:{text}")
            item_info['item_name']=text
        print(f"item_info:{item_info},count:{count}")
        if count%group_size==0:
            grouped_list.append(item_info)
            item_info = {}  # a new row
        else:
            if count==num:  #last row
                grouped_list.append(item_info)
        count=count+1
         
    print(f"grouped_list:{grouped_list}")
    return grouped_list

def get_neighbor_chars(s, index):
    if index > 0 and index < len(s) - 1:
        return s[index - 1], s[index + 1]
    else:
        return '(begin)', '(end)' if index == 0 else '(end)'