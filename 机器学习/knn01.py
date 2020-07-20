#coding=utf-8
#!/usr/bin/python
#程序简述：
#前提：
# 从MNIST官网上下载手写数据集（四个），保存在d盘的文件夹（名字为AI）中
#实验环境：
# Windows操作系统，python 3.8.2,Visual Studio Code 1.44版
#程序运行过程：
#1.程序运行时，首先读取这四个数据集
#2.输入一个k值（整数）作为kNN算法的k值
#3.程序会计算前1000个测试数据集利用KNN算法所得分类结果，计算正确率并显示
#4.输入一个i值（整数）来代表想要测试的数据
#4.对测试集中第i个数据利用kNN算法进行标签分类
#5.在中端显示区会显示测试数据的k个近邻数据的标签，测试数据的实际标签以及利用KNN算法所得的标签
#6.图像窗口显示第i个手写数据,标题为测试数据实际的标签和利用kNN算法所得的标签

'''直接读取图片的，实战中例子
def img2vector(filename):
    # 创建向量
    returnVect = np.zeros((1, 1024))
    # 打开数据文件，读取每行内容
    fr = open(filename)
    for i in range(32):
        # 读取每一行
        lineStr = fr.readline()
        # 将每行前 32 字符转成 int 存入向量
        for j in range(32):
            returnVect[0, 32*i+j] = int(lineStr[j])
    return returnVect

def classify0(inX, dataSet, labels, k):
    """
    参数:
    - inX: 用于分类的输入向量
    - dataSet: 输入的训练样本集
    - labels: 样本数据的类标签向量
    - k: 用于选择最近邻居的数目
    """
    # 获取样本数据数量
    dataSetSize = dataSet.shape[0]
    # 矩阵运算，计算测试数据与每个样本数据对应数据项的差值
    diffMat = np.tile(inX, (dataSetSize, 1)) - dataSet
    # sqDistances 上一步骤结果平方和
    sqDiffMat = diffMat**2
    sqDistances = sqDiffMat.sum(axis=1)
    # 取平方根，得到距离向量
    distances = sqDistances**0.5
    # 按照距离从低到高排序
    sortedDistIndicies = distances.argsort()
    classCount = {}
    # 依次取出最近的样本数据
    for i in range(k):
        # 记录该样本数据所属的类别
        voteIlabel = labels[sortedDistIndicies[i]]
        classCount[voteIlabel] = classCount.get(voteIlabel, 0) + 1
    # 对类别出现的频次进行排序，从高到低
    sortedClassCount = sorted(
        classCount.items(), key=operator.itemgetter(1), reverse=True)
    # 返回出现频次最高的类别
    return sortedClassCount[0][0]

def handwritingClassTest():
    # 样本数据的类标签列表
    hwLabels = []
    # 样本数据文件列表
    trainingFileList = listdir('digits/trainingDigits')
    m = len(trainingFileList)
    # 初始化样本数据矩阵（M*1024）
    trainingMat = np.zeros((m, 1024))
    # 依次读取所有样本数据到数据矩阵
    for i in range(m):
        # 提取文件名中的数字
        fileNameStr = trainingFileList[i]
        fileStr = fileNameStr.split('.')[0]
        classNumStr = int(fileStr.split('_')[0])
        hwLabels.append(classNumStr)
        # 将样本数据存入矩阵
        trainingMat[i, :] = img2vector(
            'digits/trainingDigits/%s' % fileNameStr)
    # 循环读取测试数据
    testFileList = listdir('digits/testDigits')
    # 初始化错误率
    errorCount = 0.0
    mTest = len(testFileList)
    # 循环测试每个测试数据文件
    for i in range(mTest):
        # 提取文件名中的数字
        fileNameStr = testFileList[i]
        fileStr = fileNameStr.split('.')[0]
        classNumStr = int(fileStr.split('_')[0])
        # 提取数据向量
        vectorUnderTest = img2vector('digits/testDigits/%s' % fileNameStr)
        # 对数据文件进行分类
        classifierResult = classify0(vectorUnderTest, trainingMat, hwLabels, 3)
        # 打印 K 近邻算法分类结果和真实的分类
        print("测试样本 %d, 分类器预测: %d, 真实类别: %d" %
              (i+1, classifierResult, classNumStr))
        # 判断K 近邻算法结果是否准确
        if (classifierResult != classNumStr):
            errorCount += 1.0
    # 打印错误率
    print("\n错误分类计数: %d" % errorCount)
    print("\n错误分类比例: %f" % (errorCount/float(mTest)))
'''

import numpy  as np
import operator
import struct
import matplotlib.pyplot as plt

#读取训练集标签的二进制文件，生成一个一维数组
def loadLabelSet(filename):
    binfile=open(filename,'rb')		#打开文件
    buffers=binfile.read()		#读取文件的所有内容
   # print(buffers)
    head=struct.unpack_from('>II',buffers,0)	#>表示大端模式，读取两个字节返回一个元组,第一个字节表示魔数，第二个字节表示标签数量
    labelNum=head[1]			#标签数量labelNum
   # print(head[0]) # 2049 2049
    #print(labelNum) # 60000 10000
    offset=struct.calcsize('>II')	#重新设置偏移地址
   # print(offset)  8 8
    numString='>'+str(labelNum)+'B'
   # print(numString)#格式为   >60000B   >10000B
    labels=struct.unpack_from(numString,buffers,offset)
   # print(labels)
    binfile.close()
    labels=np.reshape(labels,(labelNum))#转化为一维数组（列向量）
    return labels,labelNum

#读取训练集特征数据的二进制文件，生成一个N*M的矩阵，N表示数据的个数，M=28*28
def loadImageSet(filename):
    binfile=open(filename,'rb')		#打开文件
    buffers=binfile.read()		#读取文件的所有内容
    head=struct.unpack_from('>IIII',buffers,0)	#读取四个字节，第一个字节表示魔数，第二个字节表示数据个数，第三四个字节分别表示行数，列数
    offset=struct.calcsize('>IIII')
    imgNum=head[1]
    width=head[2]
    height=head[3]
    bits=imgNum*width*height
    bitsString='>'+str(bits)+'B'
    imgs=struct.unpack_from(bitsString,buffers,offset)
    binfile.close()
    list1=[]
    for i in range(bits):
        if imgs[i]>127:
            list1.append(1)
        else:
            list1.append(0)
    tuple(list1)
    imgs=np.reshape(list1,(imgNum,width*height))
    return imgs

# kNN: k Nearest Neighbours
# 输入： newInput:(1*N)的待分类向量，实际N=28*28
#	    dataSet:(N*M）的训练数据集，实际N=6000,M=28*28
#		labels:训练数据集的类别标签向量，每个值在0-9之间
#		k:近邻数，设置为3
#
#输出：	可能性最大的分类标签
def kNNClassify(newInput,dataSet,labels,k):
    numSample=dataSet.shape[0]		                    #训练集数据的行数(多少个训练数据)

	#step1:计算距离, P=2                                   
    #tile(A,(B,C)) 将A在行方向重复B次，在列方向重复C次,这里得到了x行28*28列的矩阵
    diff=np.tile(newInput,(numSample,1)) -dataSet	    #按元素求差值
    squaredDiff=diff**2				                    #将差值平方
    squaredDist=np.sum(squaredDiff,axis=1)		        #一行的所有元素相加
    distance=squaredDist**0.5			                #将差值平方和求开方，即为距离

	#step2:对距离排序
    sortDistIndices=np.argsort(distance)		#argsort()返回排序后的索引值
    classCount={}				                #定义一个空字典，方便添加元素,key=标签，value=该标签出现的次数
    #print('testdata\'s',k,'near data\'s label is',end=' ')
    for i in range(k):				            #0-(k-1)
       	#step3:选择k个近邻
        voteLabel=labels[sortDistIndices[i]]	#第i个近邻的标签
        #print( voteLabel,end=',')               #显示测试集数据的k个近邻的标签
	    #step4:计算k个最近邻中各类别出现的次数
        classCount[voteLabel]=classCount.get(voteLabel,0)+1
	#step5:返回出现次数最多的类别标签
    maxCount=0					                #初始化标签出现的最多次数为0
    for key,value in classCount.items():
        if value > maxCount:
            maxIndex=key
            maxCount=value
    # 下面这个同上面功能
    # sortedClassCount = sorted(
    #     classCount.items(), key=operator.itemgetter(1), reverse=True)
    # # 返回出现频次最高的类别
    # return sortedClassCount[0][0]
    return maxIndex,sortDistIndices[0:k]

def display(testX,outputLabel):
    im=np.array(testX)                      #创建一维数组im
    im=im.reshape(28,28)                    #pt修改为28*28的矩阵
    fig=plt.figure('testdata\'s label')     #创建一个窗口,窗口名字为testdata\'s label,其余属性默认
    plotwindow=fig.add_subplot(111)         #创建绘图
    plt.axis('off')
    String0="this testdata\'s truelabel is "+str(truelabel)
    String=String0+",and its prediction label is "+str(outputLabel)
    plt.title(String)
    plt.imshow(im,cmap='gray')              #im：要绘制的图像或数组，cmap:颜色
    plt.show()                              #imshow对图像进行处理，显示其格式，show函数才真正将图像显示出来
    #plt.savefig('test.png') #保存图像文件
    plt.close
    return 0


file1='C:\\Users\\asus\\Desktop\\机器智能云\\机器学习\\knnproj\\train-images.idx3-ubyte'   #训练集数据文件名（包含文件路径）
file2='C:\\Users\\asus\\Desktop\\机器智能云\\机器学习\\knnproj\\train-labels.idx1-ubyte'	#训练集标签文件名
file3='C:\\Users\\asus\\Desktop\\机器智能云\\机器学习\\knnproj\\t10k-images.idx3-ubyte'    #测试集数据文件名
file4='C:\\Users\\asus\\Desktop\\机器智能云\\机器学习\\knnproj\\t10k-labels.idx1-ubyte'    #测试集标签文件名


#生成数据集和类别标签
dataSet=loadImageSet(file1)
labels,num1=loadLabelSet(file2)
dataTest=loadImageSet(file3)
labelsTest,num2=loadLabelSet(file4)

k=int(input("input a number k as the k value of kNN algorithm:"))   #输入一个整数k,作为kNN算法的k值
num=0
for a in range(200):				           
    testX1=dataTest[a,:]		#从测试数据集和类别标签中取出第i个数据
    truelabelX1=labelsTest[a]
    outputLabelX1,sortlist=kNNClassify(testX1,dataSet,labels,k)
    if outputLabelX1==truelabelX1:
        num=num+1
print("when the k is ",k,", the accuracy is ",num/200.00)


i=int(input("input a number i as the testdata:"))                   #输入一个整数i,表示选择第i个测试数据 
testX=dataTest[i,:]		#从测试数据集和类别标签中取出第i个数据和标签
truelabel=labelsTest[i]
outputLabel,sortlist1=kNNClassify(testX,dataSet,labels,k)
print( "testdata's real label:",truelabel,",")
print( "its classified label:",outputLabel)
display(testX,outputLabel)          #用图像显示这个测试数据
# 用图像显示该测试数据的k邻近图片
for i in range(len(sortlist1)):     
    testX2=dataSet[sortlist1[i],:]
    truelabelX2=labels[sortlist1[i]]
    display(testX2,truelabelX2)
# 测试图片数
testNum=200
# 测试k取不同的值时的结果，并绘制misclassification rate图像
x=np.arange(1,10,1)
y=[]
for t in x:
    num1=0
    for b in range(testNum):				           
        testX3=dataTest[b,:]		#从测试数据集和类别标签中取出第i个数据
        truelabelX3=labelsTest[b]
        outputLabelX3,sortlist3=kNNClassify(testX3,dataSet,labels,t)
        #print( " testdata's real label:",truelabelX1,end=',')
        #print( " its classified label:",outputLabelX1)
        if outputLabelX3==truelabelX3:
            num1=num1+1
   # print(num1)
    print("when the k is ",t,", the accuracy is ",num1/float(testNum))  
    misclassification_rate=1-num1/float(testNum)
    y.append(misclassification_rate)
plt.plot(x,y,label='KNN')
plt.xlabel("K")
plt.ylabel("misclassification_rate")
plt.ylim(0,2*max(y))
plt.legend()
plt.show()

























