function [value,finalC] =evalParticle(p,data)
%load('./average database/fets1');
type=1;
p.eval=0;

k=size(data,1); % number of data points in clusters... 
[rows csize]=size(p.particle);
total=zeros(1,csize); 
elements=zeros(1,csize);
finalC=zeros(1,k);
dis=[];
for j=1:k  %% fore each data sample... in database... 
 %%% for each cluster do the following... 
 for i=1:csize
 %Squared Euclidean distance  between centers and points
 %dis(i)=distance_measure( p.particle(i) , face_fets(:,j),type);
 dis(i)=distance_measure( p.particle(:,i)' , data(j,:),type);
 end
  %% assign to cluster 
[value c]=min(dis) ;
 
 finalC(j)=c;
 total(c)=total(c)+value;
 elements(c)= elements(c)+1;
 
 end 
 %end 

p.FincalClusters=finalC;
%p.elements=elements;
 temp=zeros(1,csize); 
for i=1:csize
     if ( elements(i)>0)
     temp(i)=total(i)/elements(i);
     else 
         temp(i)=k*100;
     end 
end 

% cn=elements/k; 
% fun=sum(temp*cn);

 fitness= sum(temp) /length(temp);
value=fitness;