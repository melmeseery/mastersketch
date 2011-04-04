function gabor=GF(Lx,Ly,freq,thetta)

[x,y]=meshgrid(-(Lx/2-1):Lx/2,-(Ly/2-1):Ly/2);
X=x*cos(thetta)+y*sin(thetta);
Y=-x*sin(thetta)+y*cos(thetta);
k=pi;	
gabor=freq/sqrt(2*pi*k)*exp(-freq^2/(8*k^2)*(4*X.^2+Y.^2)).*(exp(i*freq*X)-exp(-k^2/2));

% figure
% surf(real(gabor));title ('2D Gabor Wavelet :Real Part');
% shading interp;
% 
% figure
% surf(imag(gabor));title('2D Gabor Wavelet :Imaginary Part');
% shading interp;
% 
% GABOR=fftshift(fft2(gabor));
% 
% figure
% %  surf(real(GABOR));title('2D Fourier Transform');
% pcolor(abs(GABOR));
% shading interp;

 