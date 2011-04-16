function voi = voiunvoi(x,N,Pth,Zth)
%  voiunvoi --> Voiced/unvoiced segmentation using a sliding window.
%
%  <Synopsis>
%    voi = voiunvoi(x,N,Pth,Zth)
%
%  <Description>
%    An initial voiced/unvoiced segmentation based on short-time
%    power Px and zero crossing Zx measures obtained in sliding
%    windows of length N. The middle sample in the window is labeled
%    voiced (voi=1) if Px/max(Px) > Pth and Zx < Zth.
%
%  <See Also>
%    stpower     --> Short-term power computation in a sliding window.
%    stzerocross --> Short-term zero crossing measure in a sliding window.

%  <References>
%  [1] J.R Deller, J.G. Proakis and F.H.L. Hansen, "Discrete-Time
%      Processing of Speech Signals", IEEE Press, p. 246, (2000).
%
%  <Revision>
%    Peter S.K. Hansen, IMM, Technical University of Denmark
%
%    Last revised: September 30, 2000
%-----------------------------------------------------------------------

% Check the required input arguments.
if (nargin < 4)
  error('Not enough input arguments.')
end

if (prod(size(Pth))>1) | (Pth(1,1)>1) | (Pth(1,1)<0)
  error('Requires Pth to be a scalar between 0 and 1.')
end
if (prod(size(Zth))>1) | (Zth(1,1)>1) | (Zth(1,1)<0)
  error('Requires Zth to be a scalar between 0 and 1.')
end

% Short-time power Px and zero crossing Zx measures.
Px = stpower(x,N);
Zx = stzerocross(x,N);

% Compare estimates with threshold values.
voi = (Px>Pth*max(Px)) & (Zx<Zth);

% Shift the voi-flag N/2 samples to the left (middle sample in window).
voi = [voi(fix(N/2)+1:length(voi));voi(length(voi))*ones(fix(N/2),1)];

%-----------------------------------------------------------------------
% End of function voiunvoi
%-----------------------------------------------------------------------
