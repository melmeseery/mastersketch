function dis=distance_measure(x,y,type)
if (type==1)
diff=x-y;
 mag = norm(diff);
 dis=mag;
end 