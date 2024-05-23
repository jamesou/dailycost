 
import json
import re
def is_numeric(s):
    return s.isdigit() or s.isnumeric() or is_float(s)

def is_float(s):
    try:
        float(s)
        return True
    except ValueError:
        return False

def parse_result(result):
    #4 elements grouped one row，qty is number, amount with $    
    grouped_list=[]
    # grouped_list.append({"item_name","item_qty","item_amount_unit","item_amount_unit"}) #columns header
    if len(result)>0:
        res_list = result[0]['res']
        structured_list = []
        for item in res_list:
            # print(f"item['text']:{item['text']}")
            text = item['text'].strip('.').strip()
            if len(text)>1:
                if text.find("Bag ") != -1 and text.find("deducted") != -1 :
                    print(f"find 'Bag' and 'deducted' keyword dispose the text:{text}")
                    continue
                if text == '':
                    print(f"find '' keyword dispose the text:{text}")
                    continue
                if text.find("@") != -1:
                    print(f"find '@' keyword remove the @")
                    text = text.strip('@')
                if text.find("*") != -1:
                    print(f"find '*' keyword remove the *")
                    text = text.strip('*')
                dot_index = text.find('.')
                lenth = len(text)
                before, after = get_neighbor_chars(text,dot_index)
                if dot_index!=-1 and after.isdigit() \
                and text[0] != '-' and text[0] != '$'\
                and text[lenth-1] != 'L':  #item_name contains qty,split it
                    item_name_array = [p for p in text.split(' ') if p] #remove '' character
                    item_length = len(item_name_array)-1
                    if item_length>0:
                        item_name = ''
                        for i in range(0,item_length):
                            item_name = item_name+ ' '+ item_name_array[i]
                        print(f"item_name_array:{item_name_array}")
                        item_name = item_name.strip()
                        item_qty=item_name_array[item_length].replace('O','0')
                        print(f"item_name:{item_name},item_qty:{item_qty}")
                        structured_list.append(item_name)
                        structured_list.append(item_qty)
                    else:
                        structured_list.append(text)
                else:
                    structured_list.append(text)
            else:
                 print(f"length < 1, maybe recognise error,ignore it, the text is {text}")
        group_size = 4
        item_info = {}
        print(f"structured_list:{structured_list}")
        list_size = len(structured_list)
        for i in range(0, list_size):
            if i%group_size==0:
                if i!=0:
                    grouped_list.append(item_info)
                    item_info = {}
            text = structured_list[i]
            # print(f"text:{text},i:{i}") #for debug
            if is_numeric(text):
                item_info['item_qty']=text
            elif '$'in text and '=' in text:
                item_info['item_amount_unit']=text
            elif '$'in text and '=' not in text \
                and is_numeric(text.strip('$')) \
                or is_numeric(text.strip('-$')):
                item_info['item_amount']=text
            else:
                item_info['item_name']=text
                # print(f"item_info['item_name']:{item_info['item_name']}")
            if i == list_size-1: #last row
                grouped_list.append(item_info)
    print(f"grouped_list:{grouped_list}")
    return grouped_list

def get_neighbor_chars(s, index):
    if index > 0 and index < len(s) - 1:
        return s[index - 1], s[index + 1]
    else:
        return '(begin)', '(end)' if index == 0 else '(end)'