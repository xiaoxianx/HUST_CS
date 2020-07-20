int intArrayMax(int a[],int size,max,int i)
{
	if(i>=size) return max;
	else return intArrayMax(a[],size,max>a[i]?max:a[i],i+1);
}

int intfindmax(int a[],int size)
{
	return intArrayMax(a[],siez,0,0);
}