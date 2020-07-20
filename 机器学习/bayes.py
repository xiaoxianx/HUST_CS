import numpy as np
#csv 文件格式
import csv
import matplotlib.pyplot as plt
import math
from scipy.stats import norm


#处理数据得到训练和测试的特征和标签 返回四个列表
def LoadFileToSet(file_name):
    with open(file_name) as f:
        rawCharacteristic = []  # 特征
        trainCharacteristic = []
        testCharacteristic = []
        trainLable = []
        testLable = []

        f_csv=csv.DictReader(f)
        lableName =list(f_csv.fieldnames)
        #print(lableName)
        #
        for line in f_csv.reader:
            rawCharacteristic.append(line)
        #   转换标签 male 0  female 1
        for i in range(len(rawCharacteristic)):
            rawCharacteristic[i][20]=0 if rawCharacteristic[i][20]=='male' else 1
        #打乱
        np.random.shuffle(rawCharacteristic)
        # for line in rawCharacteristic:
        #     print(line)
        #特征列数 20

        # 求每一个特征的平均值
        data_mat = np.array(rawCharacteristic).astype(float)
        count_vector = np.count_nonzero(data_mat, axis=0)
        sum_vector = np.sum(data_mat, axis=0)
        mean_vector = sum_vector / count_vector
        ## 数据缺失的地方 用 平均值填充   其实去掉对结果没啥影响
        for row in range(len(data_mat)):
            for col in range (0,20):
                if data_mat[row][col] == 0.0:
                    data_mat[row][col] = mean_vector[col]
        #划分训练集 测试集 特征和标签
        num1 = int(len(data_mat) / 3 * 2) #2112
        for i in range(num1):
            trainCharacteristic.append(data_mat[i][:20])
            trainLable.append(data_mat[i][20])
        for i in  range(num1,len(data_mat)):
            testCharacteristic.append(data_mat[i][:20])
            testLable.append(data_mat[i][20])
            #不能data_mat[：num][：20]  shape 20,21  ???
        trainLable=np.array(trainLable).astype(int)
        testLable=np.array(testLable).astype(int)
        return trainCharacteristic,testCharacteristic,trainLable,testLable


#num1 是2/3数量 2112
trainCharacteristic,testCharacteristic,trainLable,testLable=LoadFileToSet("./voice.csv")
#测试结果标签
TestResultlable=[]
# 训练集性别概率 先验概率
pmale = trainLable.sum(axis=0) / len(trainLable)
pfamle = 1 - pmale
# 训练集性别前提下的特征
trainMaleCharacteristic=[]
trainFemaleCharacteristic=[]
for i in range(len(trainLable)):
    if trainLable[i]==0: trainMaleCharacteristic.append(trainCharacteristic[i])
    else: trainFemaleCharacteristic.append(trainCharacteristic[i])
#MaleMeanVector=np.sum(trainMaleCharacteristic,axis=0)/len(trainMaleCharacteristic)
#训练男女各个特征的均值和标准差  var方差  2*20
MaleMeanVector=np.mean(trainMaleCharacteristic,axis=0)
MaleDeviationVector=np.sqrt(np.var(trainMaleCharacteristic,axis=0))
FemaleMeanVector=np.mean(trainFemaleCharacteristic,axis=0)
FemaleDeviationVector=np.sqrt(np.var(trainFemaleCharacteristic,axis=0))

#计算概率 过程取对数

BeMaleRrobablityVector=[]
BeFemaleRrobablityVector=[]
AccurateResualtNum =0
MaleAccurateResualtNum =0
FemaleAccurateResualtNum =0
for i in range(len(testLable)):
    BeMaleRrobablity = math.log2(pmale)
    BeFemaleRrobablity = math.log2(pfamle)
    for j in range(0,20):
        BeMaleRrobablity+=math.log2(norm.cdf(testCharacteristic[i][j],MaleMeanVector[j],MaleDeviationVector[j]))
        BeFemaleRrobablity+=math.log2(norm.cdf(testCharacteristic[i][j],FemaleMeanVector[j],FemaleDeviationVector[j]))

    # #不取对数
    # BeMaleRrobablity = (pmale)
    # BeFemaleRrobablity = (pfamle)
    # for j in range(0,20):
    #     BeMaleRrobablity*=norm.cdf(testCharacteristic[i][j],MaleMeanVector[j],MaleDeviationVector[j])
    #     BeFemaleRrobablity*=(norm.cdf(testCharacteristic[i][j],FemaleMeanVector[j],FemaleDeviationVector[j]))

    #print(BeMaleRrobablity,BeFemaleRrobablity)
    #BeMaleRrobablityVector.append(BeMaleRrobablity)
    #BeFemaleRrobablityVector.append(BeFemaleRrobablity)
    lable1=0 if BeMaleRrobablity>BeFemaleRrobablity else 1
    TestResultlable.append(lable1)

    if lable1==testLable[i]:
        AccurateResualtNum +=1
        if lable1==0: MaleAccurateResualtNum+=1
        else: FemaleAccurateResualtNum+=1



print("测试集数量: "+ str(len(testLable))+" 正确数量： "+ str(AccurateResualtNum)+ " 正确率： "+str(AccurateResualtNum/len(testLable)))
print("测试男数量：",len(testLable)-np.count_nonzero(testLable)," 正确数量： ",MaleAccurateResualtNum," 正确率： ",MaleAccurateResualtNum/(len(testLable)-np.count_nonzero(testLable)))
print("测试女数量：",np.count_nonzero(testLable)," 正确数量： ",FemaleAccurateResualtNum," 正确率： ",FemaleAccurateResualtNum/np.count_nonzero(testLable))


