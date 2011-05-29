function m=getRandomW(wh,N,vec)

t=vec;
mtemp=randi(wh,N); 
dim=size(vec,2);
if (dim>1)
    mat=vec;
else 
mat=repmat(vec',1,N);

end 
m=mtemp+mat; 


