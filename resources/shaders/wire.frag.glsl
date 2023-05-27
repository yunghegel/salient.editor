#ifdef GL_ES
precision highp float;
#endif

const vec4 COLOR_TURQUOISE = vec4(0,0.714,0.586, 1.0);
const vec4 COLOR_WHITE = vec4(1,1,1,1);

void main(void) {
    gl_FragColor = COLOR_TURQUOISE;
}