#ifdef GL_ES
precision mediump float;
#endif

//varying vec2 v_texCoord0;
varying vec4 v_color;

void main() {
    gl_FragColor = vec4(v_color);
}