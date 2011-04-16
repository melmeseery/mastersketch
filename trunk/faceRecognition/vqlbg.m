function r = vqlbg(d,k)
% VQLBG Vector quantization using the Linde-Buzo-Gray algorithme
%
% Inputs: d contains training data vectors (one per column)
% k is number of centroids required
%
% Output: r contains the result VQ codebook (k columns, one for each centroids)
%%
%%%%%%%%%%%%%%%%%%
% Mini-Project: An automatic speaker recognition system
%
% Responsible: Vladan Velisavljevic
% Authors: Christian Cornaz
% Urs Hunkeler



e = .01;
r = mean(d, 2);
dpr = 1000;
for i = 1:log2(k)
    i
  
   % pause
r = [r*(1+e), r*(1-e)];

while (1 == 1)
z = simmx(d, r);
[m,ind] = min(z, [], 2);
t = 0;
for j = 1:2^i
r(:, j) = mean(d(:, find(ind == j)), 2);

x = simmx(d(:, find(ind == j)), r(:, j));

for q = 1:length(x)
    
t = t + x(q);
end
end
if (((dpr - t)/t) < e)
break;
else
dpr = t;
dpr
end
end
end