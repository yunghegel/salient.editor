#ifdef GL_ES
precision mediump float;
#endif

uniform vec3 u_color;

void main(void) {
    gl_FragColor = vec4(u_color.r/255.0, u_color.g/255.0, u_color.b/255.0, 1.0);
}