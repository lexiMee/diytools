import pynmea2
import os
import sys
import xlwt

print sys.argv
if len(sys.argv) < 3 :
    sys.exit(1)

if (os.path.isfile(sys.argv[1]) != True):
    print 'fail'
    sys.exit(1)
else:
    ''' read from file
    fd = open(sys.argv[1], 'r')
    streamreader = pynmea2.NMEAStreamReader(fd)
    while 1:
        for msg in streamreader.next():
            print msg
    print 'End'
    fd.close()
    '''
    wb = xlwt.Workbook()
    ws = wb.add_sheet('NMEA')
    ws.write(0, 0, '3D FIX')
    ws.write(0, 1, '2D FIX')
    streamreader = pynmea2.NMEAStreamReader()
    fd = open(sys.argv[1], 'r');
    snr = fix3D = fix2D = 0
    index = 1
    for line in fd:
        if line.find('mnl_linux') != -1:
            if line.find('$') == -1 or line.find('GPACCURACY') != -1 or line.find('PMTK010') != -1:
                continue 
            data = line[line.index('$'):]
            for msg in streamreader.next(data):
                if msg.sentence_type == 'GGA':
                    start = 1
                elif msg.sentence_type == 'GSA':
                    if msg.mode_fix_type == '2':
                        fix2D = 1
                    elif msg.mode_fix_type == '3':
                        fix3D = 1
                    else:
                        fix2D = fix3D = 0
                elif msg.sentence_type == 'GSV':
                    snr = msg.snr_1
                elif msg.sentence_type == 'RMC':
                    if msg.data_validity == 'A':
                        if fix2D == 1:
                            ws.write(index, 1, 45)
                            ws.write(index, 0, 0)
                        elif fix3D == 1:
                            ws.write(index, 0, 55)
                            ws.write(index, 1, 0)
                    else:
                        ws.write(index, 0, 0)
                        ws.write(index, 1, 0)
                    start = fix2D = fix3D = 0
                    index += 1
        else:
            continue
    wb.save(sys.argv[2]+'.xls')
    fd.close()
    print 'end'
    
    
