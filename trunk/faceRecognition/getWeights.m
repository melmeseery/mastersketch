function vec=getWeights(data,i)
%vec=zeros(1,size(data,2)); 

%  t=sum(data);
%  vec=t/150; 
%vec=mean(data);
%vec=median(data);
%vec=data(i,:);
co=3;
all=size(data,1)/co;

%%% what about average the every other list...
% for k=1:all
%     in=(k-1)*co;
%     temp=data(in+1:in+co,:); %
%     vec(:,k)=mean(temp); 
%        
% end 
i=1;
for k=1:3:size(data,1)
    
    temp=data(k,:); %
    vec(:,i)=temp; 
    i=i+1;   
end 

